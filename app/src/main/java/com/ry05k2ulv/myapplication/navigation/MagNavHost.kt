package com.ry05k2ulv.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ry05k2ulv.myapplication.ui.generate.generateScreen
import com.ry05k2ulv.myapplication.ui.generate.navigateToGenerate
import com.ry05k2ulv.myapplication.ui.generate.select.SELECT_NAVIGATION_ROUTE
import com.ry05k2ulv.myapplication.ui.generate.select.selectScreen

@Composable
fun MagNavHost(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier ) {
        selectScreen(
            onBack = {},
            onNext = navController::navigateToGenerate
        )
        generateScreen()
    }
}