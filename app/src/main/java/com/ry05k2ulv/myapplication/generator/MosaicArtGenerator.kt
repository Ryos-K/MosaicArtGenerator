package com.ry05k2ulv.myapplication.generator

import android.graphics.Bitmap
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

import kotlin.math.sqrt


class MosaicArtGenerator(
    targetImage: Bitmap,
    gridSize: Int = DEFAULT_GRID_SIZE,
    outputSize: Int = DEFAULT_OUTPUT_SIZE
) {
    companion object {
        // Constant about grid size
        const val UNIT_SIZE = 16
        const val MIN_UNIT_PER_GRID = 1
        const val MAX_UNIT_PER_GRID = 16
        const val DEFAULT_GRID_SIZE = 32
        const val MIN_GRID_SIZE = UNIT_SIZE * MIN_UNIT_PER_GRID
        const val MAX_GRID_SIZE = UNIT_SIZE * MAX_UNIT_PER_GRID

        // Constant about output size
        const val MAX_OUTPUT_SIZE = 4096
        const val MIN_OUTPUT_SIZE = 1024
        const val DEFAULT_OUTPUT_SIZE = 2048
    }

    private val gridSize: Int = when {
        gridSize < MIN_GRID_SIZE -> MIN_GRID_SIZE
        gridSize > MAX_GRID_SIZE -> MAX_GRID_SIZE
        else -> gridSize
    }

    private val outputSize:Int = when {
        outputSize < MIN_OUTPUT_SIZE -> MIN_OUTPUT_SIZE
        outputSize > MAX_OUTPUT_SIZE -> MAX_OUTPUT_SIZE
        else -> outputSize
    }

    private val colCount: Int
    private val rowCount: Int
    init {
        if (targetImage.width > targetImage.height) {
            colCount = this.outputSize / this.gridSize
            rowCount = (((1f * this.outputSize / targetImage.width) * targetImage.height) / this.gridSize).toInt()
        } else {
            colCount = (((1f * this.outputSize / targetImage.height) * targetImage.width) / this.gridSize).toInt()
            rowCount = this.outputSize / this.gridSize
        }
    }

    private val minDissimilarityOf =
        List(colCount) { MutableList(rowCount) { Float.POSITIVE_INFINITY } }

    private val result: Bitmap

    private val target: Bitmap

    init {
        val dstWidth = colCount * this.gridSize
        val dstHeight = rowCount * this.gridSize
        result = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
        target = Bitmap.createScaledBitmap(targetImage, dstWidth, dstHeight, false)
    }

    fun getResultCopy(): Bitmap = result.copy(Bitmap.Config.ARGB_8888, false)

    fun applyMaterialImage(bitmap: Bitmap) {
        val cropped = bitmap.cropSquare()
        for (col in 0 until colCount) {
            for (row in 0 until rowCount) {
                val similarity = target.calcDissimilarity(col * gridSize, row * gridSize, cropped)
                if (similarity < minDissimilarityOf[col][row]) {
                    minDissimilarityOf[col][row] = similarity
                    result.setBitmapAt(col * gridSize, row * gridSize, cropped)
                }
            }
        }
    }

    private fun Bitmap.cropSquare(): Bitmap {
        val length = minOf(width, height)
        val top = (height - length) / 2
        val left = (width - length) / 2
        val cropped = Bitmap.createBitmap(this, left, top, length, length)
        return Bitmap.createScaledBitmap(cropped, gridSize, gridSize, false)
    }

    private fun Bitmap.calcDissimilarity(left: Int, top: Int, bitmap: Bitmap): Float {
        val pixels1 = IntArray(gridSize * gridSize)
        val pixels2 = IntArray(gridSize * gridSize)
        this.getPixels(pixels1, 0, gridSize, left, top, gridSize, gridSize)
        bitmap.getPixels(pixels2, 0, gridSize, 0, 0, gridSize, gridSize)
        var sum = 0f
        for (i in 0 until gridSize * gridSize) {
            sum += sqrt(
                0f + (pixels1[i].red - pixels2[i].red).square() +
                        (pixels1[i].green - pixels2[i].green).square() +
                        (pixels1[i].blue - pixels2[i].blue).square()
            )
        }
        return sum
    }

    private fun Bitmap.setBitmapAt(left: Int, top: Int, bitmap: Bitmap) {
        val pixels = IntArray(gridSize * gridSize)
        bitmap.getPixels(pixels, 0, gridSize, 0, 0, gridSize, gridSize)
        this.setPixels(pixels, 0, gridSize, left, top, gridSize, gridSize)
    }
}

fun Int.square() = this * this