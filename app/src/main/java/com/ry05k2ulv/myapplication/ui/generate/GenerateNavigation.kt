package com.ry05k2ulv.myapplication.ui.generate

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ry05k2ulv.myapplication.ui.generate.result.ResultScreen
import com.ry05k2ulv.myapplication.ui.generate.select.SelectScreen

const val GENERATE_NAVIGATION_ROUTE = "generate"
private sealed class Screen(val route: String) {
    object Select: Screen("select")
    object Result: Screen("result")
}

fun NavController.navigateToResult(navOptions: NavOptions? = null) {
    navigate(Screen.Result.route, navOptions)
}

fun NavController.navigateToSelect(navOptions: NavOptions? = null) {
    navigate(Screen.Select.route, navOptions)
}

fun NavGraphBuilder.generateGraph(
    navController: NavController
) {
    navigation(
        startDestination = Screen.Select.route,
        route = GENERATE_NAVIGATION_ROUTE
    ) {
        composable(
            route = Screen.Result.route
        ) {
            val generateEntry = remember(it) {
                navController.getBackStackEntry(GENERATE_NAVIGATION_ROUTE)
            }
            val generateViewModel = hiltViewModel<GenerateViewModel>(generateEntry)
            ResultScreen(generateViewModel)
        }
        composable(
            route = Screen.Select.route
        ) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(GENERATE_NAVIGATION_ROUTE)
            }
            val generateViewModel = hiltViewModel<GenerateViewModel>(parentEntry)
            SelectScreen(
                generateViewModel,
                onBack = {},
                onNext = {navController.navigateToResult()}
            )
        }
    }
}