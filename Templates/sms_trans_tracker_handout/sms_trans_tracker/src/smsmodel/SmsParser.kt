package com.check.bank.balance.banking.tool.smsmodel

import kotlin.math.exp

// ---------------------------------------------------------------- SmsParser

data class ParsedSms(
    val isBankTxn: Boolean,
    val bankSpan: String,      // raw span; map via Bank_List lookup for canonical name+logo
    val accountNumber: String,
    val amount: String,
    val avlBal: String,
    val merchantName: String,
    val transactionType: String, // CREDIT / DEBIT / OTHER
    val typeOf: String,          // VIA_BANK / VIA_CARD / NONE
    val confidence: Float,       // min head softmax confidence — gate on this
)

class SmsParser(private val w: ModelWeights) {
    private val tags = listOf("O","B-BANK","I-BANK","B-ACCOUNT","I-ACCOUNT",
        "B-AMOUNT","I-AMOUNT","B-BALANCE","I-BALANCE","B-MERCHANT","I-MERCHANT")
    private val txnTypes = listOf("CREDIT", "DEBIT", "OTHER")
    private val sources = listOf("VIA_BANK", "VIA_CARD", "NONE")

    // NOTE: GruCell carries mutable scratch buffers, so it must NOT be an
    // instance field — a single shared SmsParser (see MlParserHolder) parsing
    // two messages concurrently on Dispatchers.Default corrupted each other's
    // output (measured: ~0.5% of parses returned a wrong/empty amount under
    // 8-thread load). Created per call instead: parse() is now reentrant and
    // safe to call from parallel coroutines.
    fun parse(body: String, address: String = ""): ParsedSms {
        val fwd = GruCell(w.wIhF, w.wHhF, w.bIhF, w.bHhF, w.hidden)
        val bwd = GruCell(w.wIhB, w.wHhB, w.bIhB, w.bHhB, w.hidden)
        // prepend sender address exactly like the training generator: "<address> | <body>"
        val full = if (address.isNotBlank()) "$address | $body" else body
        val (tokens, triIds) = Tokenizer.tokenize(full)
        if (tokens.isEmpty()) return empty()

        val T = tokens.size
        val H = w.hidden

        // Embed: MEAN of the token's trigram embeddings (v2). This is what makes
        // word forms ("debit"/"debited"/"debiting") share representation, and
        // what the self-supervised pretraining phase exploited to discover that
        // money-movement verbs are interchangeable. v1 used one id per token.
        val x = Array(T) { t ->
            val tris = triIds[t]
            FloatArray(w.embDim) { c ->
                var s = 0f
                for (id in tris) s += w.emb[id, c]
                s / tris.size
            }
        }

        // biGRU
        val hF = Array(T) { FloatArray(H) }
        val hB = Array(T) { FloatArray(H) }
        val h = FloatArray(H)
        for (t in 0 until T) { fwd.step(x[t], h); System.arraycopy(h, 0, hF[t], 0, H) }
        java.util.Arrays.fill(h, 0f)
        for (t in T - 1 downTo 0) { bwd.step(x[t], h); System.arraycopy(h, 0, hB[t], 0, H) }

        // concat [fwd;bwd] per token + mean pool
        val enc = Array(T) { t -> FloatArray(2 * H).also { e ->
            System.arraycopy(hF[t], 0, e, 0, H); System.arraycopy(hB[t], 0, e, H, H)
        } }
        val pooled = FloatArray(2 * H)
        for (t in 0 until T) for (c in 0 until 2 * H) pooled[c] += enc[t][c] / T

        // heads
        val tagScores = Array(T) { t -> linear(enc[t], w.tagW, w.tagB) }
        val path = CrfDecoder.decode(tagScores, w.crfTrans, w.numTags)
        val isBank = softmax(linear(pooled, w.isBankW, w.isBankB))
        val ttype = softmax(linear(pooled, w.ttypeW, w.ttypeB))
        val src = softmax(linear(pooled, w.srcW, w.srcB))

        val fields = mergeSpans(tokens, path)
        // Confidence = the "is this a transaction?" head only. ttype/src
        // uncertainty must not kill a row whose amount/account are cleanly
        // extracted and separately validated by the KT guardrails — a real
        // Kotak txn was being dropped because a cross-bank VPA domain
        // (mfautopay.elements@hdfcbank from a Kotak sender) made the
        // source head unsure while every extracted field was correct.
        val conf = isBank.max()

        return ParsedSms(
            isBankTxn = argmax(isBank) == 1,
            bankSpan = fields["BANK"] ?: "",
            accountNumber = normalizeAccount(fields["ACCOUNT"] ?: ""),
            amount = fields["AMOUNT"] ?: "",
            avlBal = fields["BALANCE"] ?: "",
            merchantName = fields["MERCHANT"] ?: "",
            transactionType = txnTypes[argmax(ttype)],
            typeOf = sources[argmax(src)],
            confidence = conf,
        )
    }

    /** Same span-merge logic as Python decode_to_fields — first span of each type wins. */
    private fun mergeSpans(tokens: List<String>, path: IntArray): Map<String, String> {
        val spans = ArrayList<Pair<String, MutableList<String>>>()
        var curType: String? = null
        var cur = mutableListOf<String>()
        fun flush() { curType?.let { spans.add(it to cur) }; curType = null; cur = mutableListOf() }
        for (i in tokens.indices) {
            val tag = tags[path[i]]
            when {
                tag == "O" -> flush()
                tag.startsWith("B-") -> { flush(); curType = tag.substring(2); cur.add(tokens[i]) }
                else -> { // I-*
                    val e = tag.substring(2)
                    if (curType == e) cur.add(tokens[i]) else { flush(); curType = e; cur.add(tokens[i]) }
                }
            }
        }
        flush()
        val out = HashMap<String, String>()
        for ((type, toks) in spans) if (type !in out) {
            out[type] = when {
                type == "AMOUNT" || type == "ACCOUNT" || type == "BALANCE" -> toks.joinToString("")
                // VPA-shaped merchant (contains '@'): join without spaces so
                // "rahul123","@","ybl" -> "rahul123@ybl", not "rahul123 @ ybl"
                type == "MERCHANT" && toks.contains("@") -> toks.joinToString("") { it }
                else -> toks.joinToString(" ")
            }
        }
        return out
    }

    private fun linear(x: FloatArray, W: Tensor, b: FloatArray): FloatArray {
        val out = FloatArray(W.rows)
        for (r in 0 until W.rows) {
            var s = b[r]
            for (c in x.indices) s += W[r, c] * x[c]
            out[r] = s
        }
        return out
    }

    private fun softmax(x: FloatArray): FloatArray {
        val m = x.max(); var sum = 0f
        val e = FloatArray(x.size) { exp(x[it] - m).also { v -> sum += v } }
        for (i in e.indices) e[i] /= sum
        return e
    }

    private fun argmax(x: FloatArray): Int {
        var a = 0; for (i in 1 until x.size) if (x[i] > x[a]) a = i; return a
    }

    private fun empty() = ParsedSms(false, "", "", "", "", "", "OTHER", "NONE", 0f)

    // Mirrors training-side Fix 1: XX5665 / 5665 / *5665 / X5665 / ...5665
    // all mean the same account. Reduce to trailing digits so the app groups
    // them as ONE account instead of several. Returns "" if no digit run of
    // length >= 3 exists (guards against a stray "No"/mask fragment).
    private fun normalizeAccount(raw: String): String {
        val digits = Regex("""\d{3,}""").findAll(raw.trim())
            .map { it.value }.lastOrNull() ?: return ""
        return digits
    }
}