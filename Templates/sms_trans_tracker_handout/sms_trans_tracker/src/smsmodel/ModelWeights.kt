package com.check.bank.balance.banking.tool.smsmodel

import java.io.DataInputStream
import java.io.InputStream

// ------------------------------------------------------------- ModelWeights

class Tensor(val data: FloatArray, val rows: Int, val cols: Int) {
    operator fun get(r: Int, c: Int) = data[r * cols + c]
}

class ModelWeights private constructor(
    val vocabSize: Int, val embDim: Int, val hidden: Int, val numTags: Int, val maxTri: Int,
    val emb: Tensor,
    val wIhF: Tensor, val wHhF: Tensor, val bIhF: FloatArray, val bHhF: FloatArray,
    val wIhB: Tensor, val wHhB: Tensor, val bIhB: FloatArray, val bHhB: FloatArray,
    val tagW: Tensor, val tagB: FloatArray,
    val isBankW: Tensor, val isBankB: FloatArray,
    val ttypeW: Tensor, val ttypeB: FloatArray,
    val srcW: Tensor, val srcB: FloatArray,
    val crfTrans: Tensor,
) {
    companion object {
        fun load(input: InputStream): ModelWeights = DataInputStream(input.buffered()).use { d ->
            val magic = ByteArray(4); d.readFully(magic)
            require(String(magic) == "SMSM") { "bad model file magic" }
            val version = d.readIntLE(); require(version == 2) { "unsupported model version $version (expected 2)" }
            val vocab = d.readIntLE(); val emb = d.readIntLE(); val hid = d.readIntLE(); val tags = d.readIntLE(); val maxTri = d.readIntLE()

            fun tensor(rows: Int, cols: Int): Tensor {
                val scale = d.readFloatLE()
                val count = d.readIntLE()
                require(count == rows * cols) { "tensor size mismatch: got $count want ${rows * cols}" }
                val bytes = ByteArray(count); d.readFully(bytes)
                val f = FloatArray(count) { bytes[it] * scale }
                return Tensor(f, rows, cols)
            }
            fun vec(n: Int): FloatArray = tensor(n, 1).data

            // 18 tensors, EXACT export order from the training notebook (Cell "Quantize + export")
            val embT = tensor(vocab, emb)
            val wIhF = tensor(3 * hid, emb); val wHhF = tensor(3 * hid, hid)
            val bIhF = vec(3 * hid); val bHhF = vec(3 * hid)
            val wIhB = tensor(3 * hid, emb); val wHhB = tensor(3 * hid, hid)
            val bIhB = vec(3 * hid); val bHhB = vec(3 * hid)
            val tagW = tensor(tags, 2 * hid); val tagB = vec(tags)
            val isBankW = tensor(2, 2 * hid); val isBankB = vec(2)
            val ttypeW = tensor(3, 2 * hid); val ttypeB = vec(3)
            val srcW = tensor(3, 2 * hid); val srcB = vec(3)
            val crf = tensor(tags, tags)

            ModelWeights(vocab, emb, hid, tags, maxTri, embT,
                wIhF, wHhF, bIhF, bHhF, wIhB, wHhB, bIhB, bHhB,
                tagW, tagB, isBankW, isBankB, ttypeW, ttypeB, srcW, srcB, crf)
        }

        // model file is little-endian; DataInputStream is big-endian -> swap
        private fun DataInputStream.readIntLE() = Integer.reverseBytes(readInt())
        private fun DataInputStream.readFloatLE() = Float.fromBits(Integer.reverseBytes(readInt()))
    }
}
