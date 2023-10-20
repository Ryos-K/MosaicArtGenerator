package com.ry05k2ulv.myapplication.generator

import android.graphics.Bitmap
import com.ry05k2ulv.myapplication.generator.dissimilarityCalculatorImpl.MediumCalculator
import com.ry05k2ulv.myapplication.generator.dissimilarityCalculatorImpl.SpeedCalculator
import com.ry05k2ulv.myapplication.utils.square


class MosaicArtGenerator(
    targetImage: Bitmap,
    generatorConfig: GeneratorConfig
) {
    private val gridSize = generatorConfig.gridSize
    private val outputSize = generatorConfig.outputSize

    private val colCount: Int
    private val rowCount: Int

    init {
        if (targetImage.width > targetImage.height) {
            colCount = outputSize / gridSize
            rowCount =
                (((1f * outputSize / targetImage.width) * targetImage.height) / gridSize).toInt()
        } else {
            colCount =
                (((1f * outputSize / targetImage.height) * targetImage.width) / gridSize).toInt()
            rowCount = outputSize / gridSize
        }
    }

    private val result: Bitmap

    private val target: Bitmap

    init {
        val dstWidth = colCount * gridSize
        val dstHeight = rowCount * gridSize
        result = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
        target = Bitmap.createScaledBitmap(targetImage, dstWidth, dstHeight, false)
    }

    private val calculator: DissimilarityCalculator

    init {
        when (generatorConfig.priority) {
            GeneratorPriority.QUALITY -> TODO()
            GeneratorPriority.MEDIUM -> calculator =
                MediumCalculator(target, gridSize, colCount, rowCount)

            GeneratorPriority.SPEED -> calculator =
                SpeedCalculator(target, gridSize, colCount, rowCount)
        }
    }

    fun getResultCopy(): Bitmap = result.copy(Bitmap.Config.ARGB_8888, false)

    fun applyMaterialImage(bitmap: Bitmap) {
        val cropped = bitmap.cropSquare()
        val pixels = IntArray(gridSize.square())
        cropped.getPixels(pixels, 0, gridSize, 0, 0, gridSize, gridSize)
        calculator.calc(cropped).forEach { (col, row) ->
            result.setPixels(
                pixels, 0, gridSize, col * gridSize, row * gridSize, gridSize, gridSize
            )
        }
    }

    private fun Bitmap.cropSquare(): Bitmap {
        val length = minOf(width, height)
        val top = (height - length) / 2
        val left = (width - length) / 2
        val cropped = Bitmap.createBitmap(this, left, top, length, length)
        return Bitmap.createScaledBitmap(cropped, gridSize, gridSize, false)
    }
}

