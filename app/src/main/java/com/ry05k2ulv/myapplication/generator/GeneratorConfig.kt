package com.ry05k2ulv.myapplication.generator

import androidx.annotation.IntRange

data class GeneratorConfig(
    @IntRange(from = MIN_GRID_SIZE.toLong(), to = MAX_GRID_SIZE.toLong())
    val gridSize: Int = DEFAULT_GRID_SIZE,
    val outputExtension: OutputExtension = OutputExtension.PNG,
    val outputSize: Int = DEFAULT_OUTPUT_SIZE,
    val priority: GeneratorPriority = GeneratorPriority.MEDIUM
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
        const val MIN_OUTPUT_SIZE = 1024
        const val DEFAULT_OUTPUT_SIZE = 2048
    }

    init {
        require(gridSize in MIN_GRID_SIZE..MAX_GRID_SIZE) {
            "'gridSize' must be between $MIN_GRID_SIZE and $MAX_GRID_SIZE"
        }
        require(outputSize in MIN_OUTPUT_SIZE..outputExtension.maxSize) {
            "'outputSize' must be between $MIN_OUTPUT_SIZE and ${outputExtension.maxSize}"
        }
    }
}

enum class OutputExtension(val maxSize: Int) {
    PNG(4096),
    JPG(8192)
}

enum class GeneratorPriority {
    QUALITY,
    MEDIUM,
    SPEED
}