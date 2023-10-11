package com.ry05k2ulv.myapplication.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HOME_NAVIGATION_ROUTE = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onFabClick: () -> Unit
) {
    composable(
        route = HOME_NAVIGATION_ROUTE
    ) {
        HomeScreen(
            onFabClick = onFabClick
        )
    }
}