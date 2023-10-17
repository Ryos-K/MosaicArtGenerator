package com.ry05k2ulv.myapplication.ui.generate.input

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

internal const val INPUT_NAVIGATION_ROUTE = "input"

fun NavController.navigateToSelect(navOptions: NavOptions? = null) {
    navigate(INPUT_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.inputScreen(
    onFinish: (Uri, Set<Uri>, Int, Int) -> Unit
) {
    composable(
        route = INPUT_NAVIGATION_ROUTE,
    ) {
        InputScreen(
            onFinish = onFinish
        )
    }
}