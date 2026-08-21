package com.check.bank.balance.banking.tool.smsmodel

/**
 * Tokenizer v2 — MULTI-TRIGRAM ids per token.
 *
 * v1 hashed each token into ONE bucket (all its trigrams joined then hashed),
 * so "debit", "debited" and "debiting" landed in three unrelated buckets and
 * could only be related if a training template happened to cover each form.
 *
 * v2 hashes EVERY trigram separately and the model averages their embeddings.
 * "debit"/"debited"/"debiting" share deb/ebi/bit, so their vectors overlap by
 * construction — word-form relatedness exists in the representation itself,
 * not in hand-written templates. This is what lets the self-supervised
 * pretraining phase discover that money-movement verbs are interchangeable.
 *
 * MUST match pretrain.py::trigram_ids byte-for-byte. Verified by the
 * Python->Kotlin parity fixture (see TestParity).
 */
object Tokenizer {
    const val VOCAB_SIZE = 4096
    const val MAX_TRI = 8                 // cap on trigrams averaged per token
    private val TOKEN_RE = Regex("[A-Za-z0-9]+|[^\\sA-Za-z0-9]")

    // Closed, deterministic future-tense phrase list — must match FUTURE_MARKERS
    // in the trainer. When present, one synthetic token is prepended: gives the
    // classifier an explicit future-vs-completed signal with no tensor changes.
    // US model (sms_model_us.bin) added "scheduled for" / "scheduled on": US bank
    // SMS phrase future-dated payments that way ("is scheduled for 05/28"), and
    // without them the future token never fires on those. Trainer list in
    // Countries/United_States/sms_parser/trainer/tok.py must stay identical --
    // a marker on one side only shifts every embedding lookup by one position.
    private val FUTURE_MARKERS = listOf("will be", "shall be", "is scheduled to", "is due to",
        "is going to", "is expected to", "will get", "shall get", "would be",
        "scheduled for", "scheduled on")
    private const val FUTURE_TOKEN = "futuremarkertoken"

    fun hasFutureMarker(text: String): Boolean {
        val t = text.lowercase()
        return FUTURE_MARKERS.any { t.contains(it) }
    }

    /** FNV-1a over UTF-8 bytes, 32-bit wrap. Matches Python exactly. */
    private fun fnv1a(s: String): Int {
        var h = 0x811c9dc5.toInt()
        for (b in s.toByteArray(Charsets.UTF_8)) {
            h = h xor (b.toInt() and 0xff)
            h *= 0x01000193
        }
        return h
    }

    private fun bucket(s: String): Int =
        ((fnv1a(s).toLong() and 0xffffffffL) % VOCAB_SIZE).toInt()

    /**
     * Trigram buckets for one token. Tokens shorter than 3 chars fall back to
     * the whole token as a single "trigram" — matches Python's
     * `tris = [...] or [tok]`.
     */
    fun trigramIds(tok: String): IntArray {
        if (tok.length < 3) return intArrayOf(bucket(tok))
        val n = minOf(tok.length - 2, MAX_TRI)
        return IntArray(n) { bucket(tok.substring(it, it + 3)) }
    }

    /**
     * Returns raw tokens plus, per token, its trigram buckets.
     * Prepends the future-marker token when detected on the full text.
     */
    fun tokenize(text: String): Pair<List<String>, List<IntArray>> {
        val raw = TOKEN_RE.findAll(text.lowercase()).map { it.value }.toMutableList()
        if (hasFutureMarker(text)) raw.add(0, FUTURE_TOKEN)
        return raw to raw.map { trigramIds(it) }
    }
}
