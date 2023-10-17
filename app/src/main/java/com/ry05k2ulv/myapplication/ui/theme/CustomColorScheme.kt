package com.ry05k2ulv.myapplication.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColorScheme(
    val resultBackground: Color = Color.Unspecified,
    val resultContent: Color = Color.Unspecified
)

internal val LocalCustomColorScheme = staticCompositionLocalOf { CustomColorScheme() }

