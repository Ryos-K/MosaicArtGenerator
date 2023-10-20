package com.ry05k2ulv.myapplication.generator.dissimilarityCalculatorImpl

import android.graphics.Bitmap
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.ry05k2ulv.myapplication.generator.DissimilarityCalculator
import com.ry05k2ulv.myapplication.utils.square
import kotlin.math.sqrt

class MediumCalculator(
    target: Bitmap,
    private val gridSize: Int,
    private val colCount: Int,
    private val rowCount: Int,
): DissimilarityCalculator {

    private val targetPixelsOfGrid = List(colCount) { col -> List(rowCount) { row ->
        val pixels = IntArray(gridSize.square())
        target.getPixels(pixels, 0, gridSize, col * gridSize, row * gridSize, gridSize, gridSize)
        pixels
    } }

    private val minDissimilarityOfGrid =
        List(colCount) { MutableList(rowCount) { Float.POSITIVE_INFINITY } }

    override fun calc(material: Bitmap): List<Pair<Int, Int>> {
        val pixels = IntArray(gridSize.square())
        material.getPixels(pixels, 0, gridSize, 0, 0, gridSize, gridSize)

        val replaceList = mutableListOf<Pair<Int, Int>>()
        for (col in 0 until colCount) {
            for (row in 0 until rowCount) {
                if (shouldReplace(col, row, pixels)) {
                    replaceList.add(col to row)
                }
            }
        }
        return replaceList
    }

    private fun shouldReplace(col: Int, row: Int, pixels: IntArray): Boolean {
        val targetPixels = targetPixelsOfGrid[col][row]
        val dissimilarity = minDissimilarityOfGrid[col][row]
        var sum = 0f
        for (i in 0 until gridSize.square()) {
            sum += sqrt(
                0f + (targetPixels[i].red - pixels[i].red).square() +
                        (targetPixels[i].green - pixels[i].green).square() +
                        (targetPixels[i].blue - pixels[i].blue).square()
            )
            if (sum >= dissimilarity)
                return false
        }
        minDissimilarityOfGrid[col][row] = sum
        return true
    }
}