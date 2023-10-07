package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SELECT_NAVIGATION_ROUTE = "select"

fun NavController.navigateToSelect(navOptions: NavOptions?) {
    navigate(SELECT_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.selectScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    composable(
        route = SELECT_NAVIGATION_ROUTE
    ) {
        SelectScreen(
            onBack = onBack,
            onNext = onNext
        )
    }
}