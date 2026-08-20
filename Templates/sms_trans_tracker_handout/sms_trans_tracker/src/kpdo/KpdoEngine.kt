package com.check.bank.balance.banking.tool.kpdo

import android.content.Context
import com.check.bank.balance.banking.tool.constants.Constant
import com.check.bank.balance.banking.tool.model.BankSMSModell
import com.check.bank.balance.banking.tool.viewModelModules.bankTransactionFilterMl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * One SMS row as read from the device. Everything the message TEXT cannot tell
 * us must be supplied here — the model can only produce what is written in the
 * body, so date/messageId/typeId/thread come from SMS metadata.
 */
data class SmsInput(
    val address: String?,
    val body: String,
    val date: String,          // epoch millis as String, matching BankSMSModell.date
    val massageId: String,
    val typeID: String,
    val thread: Long? = 0L,
)

/** What Kpdo produces: rows to insert, plus what it dropped and why. */
data class KpdoResult(
    val rows: List<BankSMSModell>,
    val notTransaction: Int,        // model/guardrails said "not a transaction"
    val pairedAway: Int,            // notice half of a notice+ledger pair
    val duplicates: Int,            // same account+amount+date already present
    val balanceMismatches: List<String>,  // rows whose balance chain looks wrong
)

/**
 * KPDO — batch SMS -> transaction rows, in ONE call.
 *
 *     val result = KpdoEngine.process(context, inputs)
 *     dao.insertAll(result.rows)
 *
 * Why a batch engine and not just a loop:
 *
 *  - Some single money movements arrive as TWO SMS (HDFC NACH sends a
 *    "PAYMENT ALERT ... UMRN" notice AND an "UPDATE: ... debited ... Avl bal"
 *    ledger entry). Parsed one at a time, both look like valid debits and get
 *    saved twice. Their refs differ, so ref-dedup cannot pair them, and Room's
 *    unique(body,date) index cannot either — the bodies genuinely differ.
 *    Only a cross-message pass can drop the notice and keep the ledger row.
 *
 *  - Messages with no account (E-Mandate, statements) inherit the account last
 *    seen for that bank. Done per-message that depends on ARRIVAL order; done
 *    here it is resolved in CHRONOLOGICAL order, which is what was intended.
 *
 *  - Consecutive rows on one account carry running balances, so
 *    prev_balance +/- amount ~= new_balance is a free arithmetic check on the
 *    money path. That catches truncated/misread amounts no single-message
 *    guardrail can see.
 *
 * NOTE: the model itself does not learn or accumulate anything. Weights are
 * frozen; all cross-message knowledge lives in Pass 2 below, in Kotlin.
 */
object KpdoEngine {

    private const val PAIR_WINDOW_MS = 3L * 24 * 60 * 60 * 1000   // notice vs ledger, same movement

    suspend fun process(context: Context, inputs: List<SmsInput>): KpdoResult =
        withContext(Dispatchers.Default) {

            // Deterministic order first. Pass 2 is stateful, so the same input
            // must always give the same output — never rely on insertion order.
            val sorted = inputs.sortedBy { it.date.toLongOrNull() ?: 0L }

            // ---------- PASS 1: per message, stateless ----------
            // Reuses the existing filter unchanged, so every guardrail, gate and
            // fallback already in BankTransactionFilterMl still applies.
            val cardNoCache = mutableSetOf<String>()
            val parsed = ArrayList<Pair<SmsInput, BankSMSModell>>(sorted.size)
            var notTxn = 0
            for (input in sorted) {
                val row = bankTransactionFilterMl(
                    context = context,
                    body = input.body,
                    address = input.address,
                    date = input.date,
                    messageId = input.massageId,
                    typeId = input.typeID,
                    threadId = input.thread ?: 0L,
                    cardNoCache = cardNoCache,
                )
                if (row == null) notTxn++ else parsed.add(input to row)
            }

            // ---------- PASS 2: across messages, stateful ----------
            val kept = ArrayList<BankSMSModell>(parsed.size)
            val pairedIdx = HashSet<Int>()
            var duplicates = 0
            val mismatches = ArrayList<String>()

            // 2a. notice + ledger pairing.
            // A "notice" announces a movement (carries a mandate/UMRN reference,
            // no available balance). The "ledger entry" records it (carries a
            // balance). Same account + same amount inside the window = one event.
            for (i in parsed.indices) {
                if (i in pairedIdx) continue
                val (inA, rowA) = parsed[i]
                for (j in i + 1 until parsed.size) {
                    if (j in pairedIdx) continue
                    val (inB, rowB) = parsed[j]
                    if (!sameMovement(rowA, rowB, inA, inB)) continue
                    val noticeIdx = when {
                        isNotice(inA.body) && !isNotice(inB.body) -> i
                        isNotice(inB.body) && !isNotice(inA.body) -> j
                        // both/neither look like a notice -> prefer keeping the
                        // one that carries a balance (the ledger entry)
                        hasBalance(rowA) && !hasBalance(rowB) -> j
                        hasBalance(rowB) && !hasBalance(rowA) -> i
                        else -> -1                                  // ambiguous: keep both
                    }
                    if (noticeIdx >= 0) pairedIdx.add(noticeIdx)
                    break
                }
            }

            // 2b. dedup on (account, amount, date-day, type) for rows that the
            // unique(body,date) index cannot catch because bodies differ.
            // NOTE: deliberately does NOT dedupe identical repeat payments that
            // carry different refs — two genuine Rs.79 JIO payments must survive.
            val seen = HashSet<String>()

            // 2c. balance chain per account, in chronological order.
            val lastBalance = HashMap<String, Double>()

            for (i in parsed.indices) {
                if (i in pairedIdx) continue
                val (input, row) = parsed[i]

                // With a ref, the ref identifies the transaction. WITHOUT one,
                // fall back to body+timestamp: two genuine same-day same-amount
                // withdrawals are NOT duplicates just because they look alike.
                val ref = refOf(input.body)
                val dedupKey = if (ref.isNotEmpty())
                    listOf(row.accountNumber, numeric(row.amount), row.transactiontype,
                        dayOf(input.date), ref).joinToString("|")
                else
                    listOf(row.accountNumber, numeric(row.amount), row.transactiontype,
                        input.date, input.body).joinToString("|")
                if (!seen.add(dedupKey)) { duplicates++; continue }

                val bal = numeric(row.avlBal)
                val amt = numeric(row.amount)
                val key = row.bankName + "|" + row.accountNumber
                val prev = lastBalance[key]
                if (prev != null && bal != null && amt != null && bal > 0.0 && amt > 0.0) {
                    val expectDebit = prev - amt
                    val expectCredit = prev + amt
                    val tol = 1.0
                    val fitsDebit = kotlin.math.abs(bal - expectDebit) <= tol
                    val fitsCredit = kotlin.math.abs(bal - expectCredit) <= tol
                    // Only report when the balance matches the OPPOSITE direction
                    // or neither — a missing intermediate SMS also breaks the
                    // chain, so this is a signal to review, not grounds to drop.
                    if (!fitsDebit && !fitsCredit) {
                        mismatches.add("${row.accountNumber} ${row.transactiontype} ${row.amount}: bal $prev -> $bal")
                    } else if (fitsCredit && row.transactiontype == Constant.DEBIT) {
                        mismatches.add("${row.accountNumber} typed DEBIT but balance rose: $prev -> $bal")
                    } else if (fitsDebit && row.transactiontype == Constant.CREDIT) {
                        mismatches.add("${row.accountNumber} typed CREDIT but balance fell: $prev -> $bal")
                    }
                }
                if (bal != null && bal > 0.0) lastBalance[key] = bal

                kept.add(row)
            }

            KpdoResult(
                rows = kept,
                notTransaction = notTxn,
                pairedAway = pairedIdx.size,
                duplicates = duplicates,
                balanceMismatches = mismatches,
            )
        }

    // ---------------------------------------------------------------- helpers

    private val NOTICE_MARK = Regex(
        """\bumrn\b|\bumn\b|\bmandate\b|payment\s+alert|""" +
                // card bill-payment acknowledgement: "PAYMENT OF Rs.X RECEIVED TOWARDS
                // YOUR CREDIT CARD ENDING WITH 7900". A restatement of a payment that
                // is already recorded elsewhere, so it is the notice half of the pair.
                """payment\s+of\b[\s\S]{0,80}?received\s+towards|received\s+towards\s+your\s+(?:credit|debit)\s+card""",
        RegexOption.IGNORE_CASE
    )

    // A card ack reports AVAILABLE LIMIT, not an account balance. Treat that as
    // "no balance" so it never wins the ledger-entry tiebreak below.
    private val LIMIT_ONLY = Regex("""(?:available|avl)\s*limit""", RegexOption.IGNORE_CASE)
    private val REF_RE = Regex("""(?:ref(?:no|erence)?\.?\s*(?:no\.?)?|utr|txn\s*id)\s*[:\-]?\s*([A-Za-z0-9]{6,})""", RegexOption.IGNORE_CASE)
    private val NUM_RE = Regex("""[\d,]+(?:\.\d{1,2})?""")

    private fun isNotice(body: String) = NOTICE_MARK.containsMatchIn(body)

    private fun hasBalance(row: BankSMSModell) =
        (numeric(row.avlBal) ?: 0.0) > 0.0 && !LIMIT_ONLY.containsMatchIn(row.body)

    private fun numeric(v: String?): Double? {
        if (v.isNullOrBlank()) return null
        val m = NUM_RE.find(v) ?: return null
        return m.value.replace(",", "").toDoubleOrNull()
    }

    private fun refOf(body: String): String =
        REF_RE.find(body)?.groupValues?.get(1)?.uppercase() ?: ""

    private fun dayOf(dateMillis: String): String {
        val ms = dateMillis.toLongOrNull() ?: return dateMillis
        return (ms / (24L * 60 * 60 * 1000)).toString()
    }

    /** Same money movement announced twice? Same account + amount + close in time. */
    private fun sameMovement(
        a: BankSMSModell, b: BankSMSModell, ia: SmsInput, ib: SmsInput
    ): Boolean {
        if (a.accountNumber.isBlank() || a.accountNumber != b.accountNumber) return false
        if (a.transactiontype != b.transactiontype) return false
        val amtA = numeric(a.amount) ?: return false
        val amtB = numeric(b.amount) ?: return false
        if (kotlin.math.abs(amtA - amtB) > 0.01) return false
        val tA = ia.date.toLongOrNull() ?: return false
        val tB = ib.date.toLongOrNull() ?: return false
        if (kotlin.math.abs(tA - tB) > PAIR_WINDOW_MS) return false
        // Genuine repeat payments usually carry DIFFERENT refs — if both have a
        // ref and they differ, treat as two real transactions, not a pair.
        val rA = refOf(ia.body); val rB = refOf(ib.body)
        if (rA.isNotEmpty() && rB.isNotEmpty() && rA != rB) return false
        return true
    }
}