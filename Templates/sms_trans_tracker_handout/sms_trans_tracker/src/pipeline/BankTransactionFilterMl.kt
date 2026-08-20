// BankTransactionFilterMl.kt — drop-in replacement internals for bankTransactionFilter.
// Same signature, same BankSMSModell out, callers untouched (§0 of the plan).
package com.check.bank.balance.banking.tool.viewModelModules

import android.content.Context
import com.check.bank.balance.banking.tool.constants.Constant
import com.check.bank.balance.banking.tool.constants.Constant.CREDIT
import com.check.bank.balance.banking.tool.constants.Constant.DEBIT
import com.check.bank.balance.banking.tool.constants.Constant.OTHER
import com.check.bank.balance.banking.tool.model.BankSMSModell
import com.check.bank.balance.banking.tool.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.check.bank.balance.banking.tool.smsmodel.BankSenderResolver
import com.check.bank.balance.banking.tool.smsmodel.ModelWeights
import com.check.bank.balance.banking.tool.smsmodel.SmsParser
import java.math.BigDecimal

interface SmsFilterContext {
    fun openAsset(fileName: String): java.io.InputStream
    suspend fun getBankName(body: String): Pair<String, String>
    suspend fun getMerchantName(body: String): String
}

class AndroidSmsFilterContext(private val context: Context) : SmsFilterContext {
    override fun openAsset(fileName: String): java.io.InputStream = context.assets.open(fileName)
    override suspend fun getBankName(body: String): Pair<String, String> = body.getBankName(context)
    override suspend fun getMerchantName(body: String): String = body.getMerchantName(context)
}

object MlParserHolder {
    // Loaded once, ~400KB dequantized floats, lives for app lifetime.
    @Volatile private var parser: SmsParser? = null
    fun get(context: Context): SmsParser = get(AndroidSmsFilterContext(context))
    fun get(env: SmsFilterContext): SmsParser =
        parser ?: synchronized(this) {
            parser ?: SmsParser(
                ModelWeights.load(env.openAsset("sms_model_v7.bin"))
            ).also { parser = it }
        }
}

/**
 * Remembers the last account confirmed per bank-key, so messages that name a
 * bank but carry no account (E-Mandate, statement notices) inherit the
 * account from this user's earlier messages instead of saving as UNKNOWN.
 *
 * Inherits only when the bank has exactly ONE known account (SOLE_ACCOUNT).
 * Refuses to guess when multiple accounts exist for that bank.
 */
object AccountMemory {
    private val accountsByBank = java.util.concurrent.ConcurrentHashMap<String, java.util.concurrent.ConcurrentHashMap.KeySetView<String, Boolean>>()

    private fun norm(key: String) = key.uppercase().replace(Regex("[^A-Z]"), "")

    fun remember(keys: List<String?>, account: String) {
        if (account.isEmpty()) return
        for (k in keys) if (!k.isNullOrEmpty()) {
            accountsByBank.computeIfAbsent(norm(k)) { java.util.concurrent.ConcurrentHashMap.newKeySet() }.add(account)
        }
    }

    fun recall(keys: List<String?>): String? {
        for (k in keys) if (!k.isNullOrEmpty()) {
            val set = accountsByBank[norm(k)]
            if (set != null && set.size == 1) return set.first()
        }
        return null
    }

    fun clear() {
        accountsByBank.clear()
    }
}

// Flip after shadow-mode diff rate is acceptable (§5 of the plan).
const val USE_ML_PARSER = true

// Bump when the model asset (or parse pipeline) changes — ReparseMigration re-runs
// once per version and rebuilds stored rows from their original bodies.
const val PARSER_VERSION = "sms_model_v7+guards9" // + credit-card body owns its own account/typeOf

// Guardrails (§4): never trust model output on the money path unvalidated.
// "... CREDIT CARD ENDING WITH 7900", "... Card ending 7900", "... card no 7900"
private val CARD_ENDING_RE = Regex(
    """(?:credit|debit)?\s*card\s*(?:no\.?|number)?\s*(?:ending\s*(?:with|in)?|x|\*+)\s*[Xx*]*(\d{4,})""",
    RegexOption.IGNORE_CASE
)

// Body identifies itself as a CREDIT card's own message. Same credit/debit phrase
// test CardSignals.classify uses, applied to one body.
private val CREDIT_CARD_BODY_RE = Regex("""credit\s*card|card\s*member""", RegexOption.IGNORE_CASE)
private val DEBIT_CARD_BODY_RE = Regex("""debit\s*card""", RegexOption.IGNORE_CASE)

private val AMOUNT_RE = Regex("""\d{1,10}(\.\d{1,2})?""")  // 1,10 not 1,7: crore-scale balances are 8-9 digits
private val ACCOUNT_RE = Regex("""(x+\d{3,}|\.{2,}\d{2,}|\d{3,})""", RegexOption.IGNORE_CASE)
private const val MIN_CONFIDENCE = 0.80f

private val QUALIFIED_BAL_RE = Regex(
    """(?:avl|avlbl|available|total|new|net|closing|clear)\.?\s*(?:bal|balance)\s*(?:is)?[:\s.]*(?:rs\.?|inr\.?|re\.?|₹|\$)?[\s]*([\d,]+(?:\.\d{1,2})?)""",
    RegexOption.IGNORE_CASE
)

private val BARE_BAL_RE = Regex(
    """(?:bal|balance)\s*(?:is)?[:\s.]*(?:rs\.?|inr\.?|re\.?|₹|\$)?[\s]*([\d,]+(?:\.\d{1,2})?)""",
    RegexOption.IGNORE_CASE
)

private val BODY_AMT_RE = Regex(
    """(?:rs\.?|inr\.?|\u20B9)\s*([\d,]+(?:\.\d{1,2})?)|(?:debited|credited|debit|credit)\s+(?:rs\.?|inr\.?|\u20B9)?\s*([\d,]{1,9}(?:\.\d{1,2})?)""",
    RegexOption.IGNORE_CASE
)

// Not preceded by a letter/digit: "CHQ0" must not offer a bare "0" token that would
// validate a garbage model amount of "00".
private val MONEY_TOKEN_RE = Regex("""(?<![A-Za-z0-9])\d[\d,]*(?:\.\d{1,2})?""")

// Indian grouping ("INR.6,87,367.28") lets the model emit one comma-group instead of
// the whole number — "6" as the amount, "87367" as the balance. Such a fragment appears
// nowhere in the body as a complete money token, so the regex reading wins.
private fun isBodyMoneyToken(value: String, body: String): Boolean {
    val v = runCatching { BigDecimal(value) }.getOrNull() ?: return false
    if (v.signum() == 0) return false            // "₹ 00" is never a real amount
    return MONEY_TOKEN_RE.findAll(body).any { tok ->
        runCatching { BigDecimal(tok.value.replace(",", "").trimEnd('.')) }
            .getOrNull()?.compareTo(v) == 0
    }
}

private fun repairTruncatedNumber(modelValue: String, body: String, re: Regex): String {
    val mv = modelValue.replace(",", "")
    val match = re.find(body)
    val m1 = match?.groupValues?.getOrNull(1)?.replace(",", "") ?: ""
    val m2 = match?.groupValues?.getOrNull(2)?.replace(",", "") ?: ""
    val m = m1.ifEmpty { m2 }
    if (m.isEmpty()) return mv
    if (mv.isEmpty() || !AMOUNT_RE.matches(mv) || (m.startsWith(mv) && m.length > mv.length) ||
        !isBodyMoneyToken(mv, body)
    ) return m
    return mv
}

private fun repairTruncatedBalance(modelValue: String, body: String): String {
    val mv = modelValue.replace(",", "")
    val match = QUALIFIED_BAL_RE.find(body) ?: BARE_BAL_RE.find(body)
    val m = match?.groupValues?.getOrNull(1)?.replace(",", "") ?: ""
    if (m.isEmpty()) return mv
    if (mv.isEmpty() || !AMOUNT_RE.matches(mv) || (m.startsWith(mv) && m.length > mv.length) ||
        !isBodyMoneyToken(mv, body)
    ) return m
    return mv
}

private fun sameMoney(a: String, b: String): Boolean {
    val x = runCatching { BigDecimal(a) }.getOrNull() ?: return false
    val y = runCatching { BigDecimal(b) }.getOrNull() ?: return false
    return x.compareTo(y) == 0
}

// The model can tag one figure as both amount and balance ("...is DEBITED Rs.100 by
// Transfer with CHQ0 Clear Balance is Rs.1963.00" -> 1963 for both). When they collide
// and the body names a different figure beside the debit/credit verb, that one is the
// amount.
private fun repairAmountAgainstBalance(
    modelAmount: String,
    modelBalance: String,
    body: String,
): String {
    val amt = repairTruncatedNumber(modelAmount, body, BODY_AMT_RE)
    if (amt.isEmpty()) return amt
    if (!sameMoney(amt, repairTruncatedBalance(modelBalance, body))) return amt
    val match = BODY_AMT_RE.find(body) ?: return amt
    val fromBody = match.groupValues.getOrNull(1)?.replace(",", "").orEmpty()
        .ifEmpty { match.groupValues.getOrNull(2)?.replace(",", "").orEmpty() }
    return if (fromBody.isNotEmpty() && !sameMoney(fromBody, amt) && AMOUNT_RE.matches(fromBody)) {
        fromBody
    } else {
        amt
    }
}

private fun digitsOfAmount(value: String): String =
    value.substringBefore('.').filter { it.isDigit() }

// Shared by the parse door (new inserts) and the one-time upgrade cleanup
// (rows written before these guards existed).
object TransactionRowValidator {

    // Only mandate CREATION notices are non-transactions. Executed autopay
    // debits ("Rs.199 debited ... as per mandate") are real money movement —
    // dropping them inflated the computed balance.
    private val MANDATE_WORD = Regex(
        """\bmandate\b|\bautopay\b|\be-?mandate\b|standing\s+instruction""",
        RegexOption.IGNORE_CASE
    )
    private val CREATION_VERB = Regex(
        """\b(created|raised|registered|approved|set\s*up|will\s+be\s+debited)\b""",
        RegexOption.IGNORE_CASE
    )

    private val OTP_WORD = Regex(
        """\b(otp|one\s*time\s*password)\b""",
        RegexOption.IGNORE_CASE
    )

    private val DECLINED_WORD = Regex(
        """\b(declined|payment\s+failed|low\s+funds\s+alert)\b""",
        RegexOption.IGNORE_CASE
    )

    private val REMINDER_WORD = Regex(
        """\bmaintain\s+balance\b|\bauto\s*debit\s+(?:due|on)\b""",
        RegexOption.IGNORE_CASE
    )

    // Cancelling a mandate moves no money either, and the notice quotes the mandate
    // amount — which was landing in avlBal and anchoring the account balance to it.
    private val CANCEL_VERB = Regex(
        """\b(cancell?ed|revoked|stopped|deleted|de-?registered)\b""",
        RegexOption.IGNORE_CASE
    )

    fun isMandateNotice(body: String): Boolean =
        MANDATE_WORD.containsMatchIn(body) &&
                (CREATION_VERB.containsMatchIn(body) || CANCEL_VERB.containsMatchIn(body))

    fun isOtpNotice(body: String): Boolean = OTP_WORD.containsMatchIn(body)

    fun isDeclinedNotice(body: String): Boolean = DECLINED_WORD.containsMatchIn(body)

    fun isReminderNotice(body: String): Boolean = REMINDER_WORD.containsMatchIn(body)

    fun isAmountAsAccount(accountNumber: String, amount: String): Boolean {
        val accDigits = accountNumber.filter { it.isDigit() }
        val amtDigits = digitsOfAmount(amount)
        return accDigits.isNotEmpty() && accDigits == amtDigits
    }

    private val AC_CONTEXT = Regex(
        """a/?c(?:count)?\s*(?:no\.?)?\s*[.:]?\s*[Xx*]*(\d{3,})""",
        RegexOption.IGNORE_CASE
    )

    // Old rows can store a garbage amount column ("₹ 00"), so the column compare
    // misses them — detect from the body: the stored account digits appear as a
    // money value while the body names a different real account.
    fun isAmountAsAccountInBody(accountNumber: String, body: String): Boolean {
        val acc = accountNumber.filter { it.isDigit() }
        if (acc.length < 3) return false
        val asMoney = Regex("""(?:rs\.?|inr|₹)\s*$acc(?:\.\d{1,2})?\b""", RegexOption.IGNORE_CASE)
        if (!asMoney.containsMatchIn(body)) return false
        val bodyAccounts = AC_CONTEXT.findAll(body).map { it.groupValues[1] }.toSet()
        return bodyAccounts.isNotEmpty() && acc !in bodyAccounts
    }

    fun isInvalidRow(accountNumber: String, amount: String, body: String): Boolean =
        accountNumber.isBlank() ||
                isMandateNotice(body) ||
                isOtpNotice(body) ||
                isDeclinedNotice(body) ||
                isReminderNotice(body) ||
                isAmountAsAccount(accountNumber, amount) ||
                isAmountAsAccountInBody(accountNumber, body)
}

suspend fun bankTransactionFilterMl(
    context: Context,
    body: String,
    address: String?,
    date: String,
    messageId: String,
    typeId: String,
    threadId: Long,
    cardNoCache: MutableCollection<String>,
): BankSMSModell? = bankTransactionFilterMl(
    env = AndroidSmsFilterContext(context),
    body = body,
    address = address,
    date = date,
    messageId = messageId,
    typeId = typeId,
    threadId = threadId,
    cardNoCache = cardNoCache
)

suspend fun bankTransactionFilterMl(
    env: SmsFilterContext,
    body: String,
    address: String?,
    date: String,
    messageId: String,
    typeId: String,
    threadId: Long,
    cardNoCache: MutableCollection<String> = mutableListOf(),
): BankSMSModell? = withContext(Dispatchers.Default) { // CPU-bound, not IO

    val p = MlParserHolder.get(env).parse(body, address ?: "")
    val senderBank = BankSenderResolver.resolve(address ?: "")
    val hasBankHint = senderBank != null || env.getBankName(body).first.isNotEmpty()


    // Not a transaction, or model unsure about that call -> keep old behavior (drop)
    if ((!p.isBankTxn || p.confidence < MIN_CONFIDENCE) && !(hasBankHint && body.checkSenderIsValid())) return@withContext null

    if (TransactionRowValidator.isMandateNotice(body) ||
        TransactionRowValidator.isOtpNotice(body) ||
        TransactionRowValidator.isDeclinedNotice(body) ||
        TransactionRowValidator.isReminderNotice(body)) return@withContext null

    // Anti-spoof gate: legit Indian transactional SMS arrives via DLT sender
    // IDs (XX-XXXXXX) or shortcodes — never from a personal 10-digit mobile.
    // A perfect-looking "transaction" from a raw number ("079901 91668") is
    // a fraud message, not a missed bank format. One-way veto: address can
    // never CONFIRM a bank (body does that), but personal-number + no
    // resolver match = drop.
    val looksPersonalNumber = (address ?: "").replace(Regex("[\\s+\\-]"), "")
        .let { it.length >= 10 && it.all(Char::isDigit) }
    if (looksPersonalNumber && senderBank == null) return@withContext null

    // Statement/bill-alert gate: card statements and bill-due notices are a
    // CLOSED keyword family ("Min.due", "Total due ... Pay by"), not money
    // movement. Deterministic here beats retraining the classifier every
    // time this borderline shape drifts — same principle as the spoof gate.
    // (Model round that tried to fix this by weight-rebalancing made
    // aggregate accuracy WORSE twice; reverted, gated here instead.)
    val lowerBody = body.lowercase()
    val isStatementAlert = lowerBody.contains("min.due") ||
            lowerBody.contains("min due") ||
            lowerBody.contains("min. due") ||
            lowerBody.contains("amount due") ||
            lowerBody.contains("total due") ||
            lowerBody.contains("total.due") ||
            lowerBody.contains("total. due") ||
            (lowerBody.contains("statement") && lowerBody.contains("due"))
    if (isStatementAlert) return@withContext null

    val lookupKey = when {
        !senderBank.isNullOrEmpty() -> senderBank
        p.bankSpan.isNotEmpty() -> p.bankSpan
        else -> (address ?: "")
    }
    var (bankName, logoCode) = env.getBankName(lookupKey)
    if (bankName.isEmpty() && !senderBank.isNullOrEmpty() && p.bankSpan.isNotEmpty()) {
        val bySpan = env.getBankName(p.bankSpan)         // resolver name not in Bank_List
        bankName = bySpan.first; logoCode = bySpan.second
    }
    if (bankName.isEmpty()) {
        val legacy = env.getBankName(body)               // legacy fallback
        bankName = legacy.first; logoCode = legacy.second
    }
    // Last resort: sender resolved a bank that Bank_List doesn't know —
    // use the resolver's name directly rather than dropping a real txn.
    if (bankName.isEmpty() && !senderBank.isNullOrEmpty()) {
        bankName = senderBank
    }
    if (bankName.isEmpty()) return@withContext null          // same drop rule as before

    // Keys used for account memory — sender-derived first (most reliable),
    // then display name, then model's raw span.
    val bankKeys = listOf(senderBank, bankName, p.bankSpan)

    // ---- field-level guardrails, legacy regex as per-field fallback ----
    val amountNum = repairAmountAgainstBalance(p.amount, p.avlBal, body)
    val amount = if (AMOUNT_RE.matches(amountNum)) "\u20B9 $amountNum"
    else body.getAmount()                       // legacy fallback
    if (amount.isEmpty()) return@withContext null            // no valid amount = not saveable

    // Legacy getAccountNumber() NEVER returns "" — on no-match it returns a
    // sentinel like "XX" or "XXNA" (that's why the old BankTransactionFilter
    // had an accountNumber == "XXNA" drop check). So validity here is judged
    // by digit content, not emptiness. Result is normalized to trailing
    // digits so legacy "XX5665" and model "5665" are ONE account, never two.
    val legacyAcct = body.getAccountNumber()
        .takeIf { it.count(Char::isDigit) >= 3 && !it.contains("NA", ignoreCase = true) }
        ?.let { raw -> Regex("""\d{3,}""").findAll(raw).lastOrNull()?.value } ?: ""

    // Amount-as-account guard: "Sent Rs.1999.00 from A/c 5665" was storing 1999
    // as the account, creating phantom account cards. An extraction that equals
    // the amount digits is treated as no-extraction.
    val amountDigits = digitsOfAmount(amount)
    fun looksLikeAmount(acc: String): Boolean {
        val d = acc.filter { it.isDigit() }
        return d.isNotEmpty() && d == amountDigits
    }
    val accountFromMessage = legacyAcct.ifEmpty {
        (if (ACCOUNT_RE.matches(p.accountNumber)) p.accountNumber.uppercase() else "")
            .takeUnless { looksLikeAmount(it) } ?: ""
    }


    // Card number named in the body -> attribute to the CARD, never inherit a
    // bank account. Deliberately NOT folded into accountFromMessage: that value
    // feeds AccountMemory.remember(), and a card number must not become the
    // bank's last-seen account.
    val cardFromBody = CARD_ENDING_RE.find(body)?.groupValues?.get(1) ?: ""

    // A body that names its own CREDIT card owns the attribution outright. The model
    // emits a sibling account (the bank a/c, or the debit card) for card-bill-payment
    // templates, which landed the green payment credit on the BANK account.
    val isCreditCardBody = cardFromBody.isNotEmpty() &&
            CREDIT_CARD_BODY_RE.containsMatchIn(body) &&
            !DEBIT_CARD_BODY_RE.containsMatchIn(body)

    val account = if (isCreditCardBody) cardFromBody else {
        accountFromMessage.ifEmpty { cardFromBody }.ifEmpty {
            // Message names a bank but carries no a/c (statements, balance alerts)
            // -> inherit the account last seen for this bank.
            AccountMemory.recall(bankKeys)?.takeUnless { looksLikeAmount(it) } ?: ""
        }
    }
    if (account.isBlank()) return@withContext null

    val balNum = repairTruncatedBalance(p.avlBal, body)
        .removeSuffix("cr.").removeSuffix("dr.")
    val avlBal = if (AMOUNT_RE.matches(balNum)) "\u20B9 $balNum" else body.getBalance()

    val mlTxnType = when (p.transactionType) {
        "CREDIT" -> CREDIT
        "DEBIT" -> DEBIT
        else -> OTHER
    }
    val keywordTxnType = body.transactionType()
    val transactionType = if (mlTxnType != keywordTxnType && keywordTxnType != OTHER) {
        keywordTxnType
    } else {
        mlTxnType
    }


    val resolvedTypeOf = when {
        isCreditCardBody -> Constant.VIA_CARD
        p.typeOf == "VIA_BANK" -> Constant.VIA_BANK
        p.typeOf == "VIA_CARD" -> Constant.VIA_CARD
        else -> ""
    }

    val merchantName = when {
        resolvedTypeOf == Constant.VIA_CARD && transactionType == CREDIT -> "Credit card bill payment"
        p.merchantName.isNotEmpty() -> p.merchantName
        else -> env.getMerchantName(body)                // legacy fallback
    }

    // Feed memory only with accounts that came from the MESSAGE itself
    // (model or legacy regex) — never re-remember an inherited one, or a
    // single stale value could echo forever.
    if (accountFromMessage.isNotEmpty()) {
        AccountMemory.remember(bankKeys, accountFromMessage)
    }

    BankSMSModell(
        id = 0,
        bankName = bankName,
        accountNumber = account,
        date = date,
        amount = amount,
        avlBal = avlBal,
        merchantName = merchantName,
        transactiontype = transactionType,
        body = body,
        address = address ?: "address",
        massageId = messageId,
        typeID = typeId,
        thread = threadId,
        logoCode = logoCode,
        typeOf = resolvedTypeOf,
    )
}