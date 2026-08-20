package com.check.bank.balance.banking.tool.smsmodel

// --------------------------------------------------------------- CrfDecoder

object CrfDecoder {
    /** Viterbi. scores: [T][numTags] emission scores. Returns best tag path. */
    fun decode(scores: Array<FloatArray>, trans: Tensor, numTags: Int): IntArray {
        val T = scores.size
        if (T == 0) return IntArray(0)
        var cur = scores[0].copyOf()
        val back = Array(T) { IntArray(numTags) }
        val next = FloatArray(numTags)
        for (t in 1 until T) {
            for (j in 0 until numTags) {
                var best = Float.NEGATIVE_INFINITY; var arg = 0
                for (i in 0 until numTags) {
                    val s = cur[i] + trans[i, j]
                    if (s > best) { best = s; arg = i }
                }
                next[j] = best + scores[t][j]
                back[t][j] = arg
            }
            System.arraycopy(next, 0, cur, 0, numTags)
        }
        var last = 0
        for (j in 1 until numTags) if (cur[j] > cur[last]) last = j
        val path = IntArray(T); path[T - 1] = last
        for (t in T - 1 downTo 1) path[t - 1] = back[t][path[t]]
        return path
    }
}
