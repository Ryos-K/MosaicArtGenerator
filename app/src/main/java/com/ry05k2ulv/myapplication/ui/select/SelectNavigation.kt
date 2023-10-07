package com.ry05k2ulv.myapplication.ui.select

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SELECT_NAVIGATION_ROUTE = "select"

fun NavController.navigateToSelect(navOptions: NavOptions?) {
    navigate(SELECT_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.selectScreen() {
    composable(
        route = SELECT_NAVIGATION_ROUTE
    ) {
        SelectScreen()
    }
}