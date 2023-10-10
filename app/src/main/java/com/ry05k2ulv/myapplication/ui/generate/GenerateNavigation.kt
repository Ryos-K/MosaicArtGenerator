package com.ry05k2ulv.myapplication.ui.generate

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ry05k2ulv.myapplication.ui.generate.result.ResultScreen
import com.ry05k2ulv.myapplication.ui.generate.result.navigateToResult
import com.ry05k2ulv.myapplication.ui.generate.result.resultScreen
import com.ry05k2ulv.myapplication.ui.generate.select.SELECT_NAVIGATION_ROUTE
import com.ry05k2ulv.myapplication.ui.generate.select.SelectScreen
import com.ry05k2ulv.myapplication.ui.generate.select.selectScreen

const val GENERATE_NAVIGATION_ROUTE = "generate"

fun NavGraphBuilder.generateGraph(
    navController: NavController
) {
    navigation(
        startDestination = SELECT_NAVIGATION_ROUTE,
        route = GENERATE_NAVIGATION_ROUTE
    ) {
        selectScreen(
            onFinish = navController::navigateToResult
        )
        resultScreen()
    }
}