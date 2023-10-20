package com.ry05k2ulv.myapplication.generator.dissimilarityCalculatorImpl

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.ry05k2ulv.myapplication.generator.DissimilarityCalculator
import com.ry05k2ulv.myapplication.utils.square
import kotlin.math.pow

/*
 * first  : Red
 * second : Green
 * third  : Blue
 */
private data class FeatureVector(
    val sum: Triple<Float, Float, Float>,
    val barycenterX: Triple<Float, Float, Float>,
    val barycenterY: Triple<Float, Float, Float>,
//    val varianceX: Triple<Long, Long, Long>,
//    val varianceY: Triple<Long, Long, Long>,
) {
    constructor(
        sum: Triple<Float, Float, Float>,
        barycenter: Pair<Triple<Float, Float, Float>, Triple<Float, Float, Float>>
    ) : this(sum, barycenter.first, barycenter.second)

    fun distance(vector: FeatureVector) =
        (this.sum.first - vector.sum.first).pow(2) +
        (this.sum.second - vector.sum.second).pow(2) +
        (this.sum.third - vector.sum.third).pow(2) +
        (this.barycenterX.first - vector.barycenterX.first).pow(2) +
        (this.barycenterX.second - vector.barycenterX.second).pow(2) +
        (this.barycenterX.third - vector.barycenterX.third).pow(2) +
        (this.barycenterY.first - vector.barycenterY.first).pow(2) +
        (this.barycenterY.second - vector.barycenterY.second).pow(2) +
        (this.barycenterY.third - vector.barycenterY.third).pow(2)
}

class SpeedCalculator(
    target: Bitmap,
    private val gridSize: Int,
    private val colCount: Int,
    private val rowCount: Int,
) : DissimilarityCalculator {

    private val targetFeatureVectorOfGrid = List(colCount) { col ->
        List(rowCount) { row ->
            val pixels = IntArray(gridSize.square())
            target.getPixels(
                pixels,
                0,
                gridSize,
                col * gridSize,
                row * gridSize,
                gridSize,
                gridSize
            )
            calcFeatureVector(pixels)
        }
    }

    private val minDissimilarityOfGrid =
        List(colCount) { MutableList(rowCount) { Float.POSITIVE_INFINITY } }

    override fun calc(material: Bitmap): List<Pair<Int, Int>> {
        val pixels = IntArray(gridSize.square())
        material.getPixels(pixels, 0, gridSize, 0, 0, gridSize, gridSize)
        val featureVector = calcFeatureVector(pixels)
        val replaceList = mutableListOf<Pair<Int, Int>>()
        for (col in 0 until colCount) {
            for (row in 0 until rowCount) {
                if (shouldReplace(col, row, featureVector))
                    replaceList.add(col to row)
            }
        }
        return replaceList
    }

    private fun shouldReplace(col: Int, row: Int, featureVector: FeatureVector): Boolean {
        val targetFeatureVector = targetFeatureVectorOfGrid[col][row]
        val dissimilarity = targetFeatureVector.distance(featureVector)
        if (dissimilarity > minDissimilarityOfGrid[col][row])
            return false
        minDissimilarityOfGrid[col][row] = dissimilarity
        return true
    }

    private fun calcFeatureVector(pixels: IntArray) =
        FeatureVector(
            sum = calcSum(pixels),
            barycenter = calcBarycenter(pixels)
        )

    private fun calcSum(pixels: IntArray): Triple<Float, Float, Float> {
        var sumRed = 0
        var sumGreen = 0
        var sumBlue = 0
        pixels.forEach {
            sumRed += it.red
            sumGreen += it.green
            sumBlue += it.blue
        }
        return Triple(
            sumRed.toFloat(),
            sumGreen.toFloat(),
            sumBlue.toFloat()
        )
    }

    private fun calcBarycenter(pixels: IntArray): Pair<Triple<Float, Float, Float>, Triple<Float, Float, Float>> {
        var redX = 0
        var redY = 0
        var greenX = 0
        var greenY = 0
        var blueX = 0
        var blueY = 0
        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                redX += x * pixels[x + y * gridSize].red
                redY += y * pixels[x + y * gridSize].red
                greenX += x * pixels[x + y * gridSize].green
                greenY += y * pixels[x + y * gridSize].green
                blueX += x * pixels[x + y * gridSize].blue
                blueY += y * pixels[x + y * gridSize].blue
            }
        }
        return Triple(
            redX.toFloat() / gridSize,
            greenX.toFloat() / gridSize,
            blueX.toFloat() / gridSize
        ) to Triple(
            redY.toFloat() / gridSize,
            greenY.toFloat() / gridSize,
            blueY.toFloat() / gridSize
        )
    }

//    private fun calcVariance(pixels: IntArray): Triple<Long, Long, Long> {
//        var varRed = 0L
//        var varGreen = 0L
//        var varBlue = 0L
//        for (x in 0 until gridSize) {
//            for (y in 0 until gridSize) {
//
//            }
//        }
//    }
}