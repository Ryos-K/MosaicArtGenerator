package com.ry05k2ulv.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ry05k2ulv.myapplication.ui.generate.generateGraph
import com.ry05k2ulv.myapplication.ui.generate.select.navigateToSelect
import com.ry05k2ulv.myapplication.ui.home.homeScreen

@Composable
fun MagNavHost(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier ) {
        homeScreen(
            onFabClick = {navController.navigateToSelect()}
        )
        generateGraph(navController)
    }
}