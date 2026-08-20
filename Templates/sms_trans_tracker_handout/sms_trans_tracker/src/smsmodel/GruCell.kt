package com.check.bank.balance.banking.tool.smsmodel

import kotlin.math.exp
import kotlin.math.tanh

// ------------------------------------------------------------------ GruCell

/** One GRU direction. PyTorch gate order in the 3*hidden weight rows: reset, update, new. */
class GruCell(
    private val wIh: Tensor, private val wHh: Tensor,
    private val bIh: FloatArray, private val bHh: FloatArray,
    private val hidden: Int,
) {
    private val gi = FloatArray(3 * hidden)
    private val gh = FloatArray(3 * hidden)

    private fun sigmoid(x: Float) = 1f / (1f + exp(-x))

    /** h := GRU step(x, h) — h updated in place. */
    fun step(x: FloatArray, h: FloatArray) {
        for (r in 0 until 3 * hidden) {
            var si = bIh[r]; var sh = bHh[r]
            for (c in x.indices) si += wIh[r, c] * x[c]
            for (c in 0 until hidden) sh += wHh[r, c] * h[c]
            gi[r] = si; gh[r] = sh
        }
        for (i in 0 until hidden) {
            val r = sigmoid(gi[i] + gh[i])                          // reset
            val z = sigmoid(gi[hidden + i] + gh[hidden + i])        // update
            val n = tanh(gi[2 * hidden + i] + r * gh[2 * hidden + i]) // new
            h[i] = (1f - z) * n + z * h[i]
        }
    }
}
