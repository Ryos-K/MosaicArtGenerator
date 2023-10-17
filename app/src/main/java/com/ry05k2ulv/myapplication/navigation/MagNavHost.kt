package com.ry05k2ulv.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ry05k2ulv.myapplication.ui.generate.generateGraph
import com.ry05k2ulv.myapplication.ui.generate.input.navigateToSelect
import com.ry05k2ulv.myapplication.ui.home.homeScreen

@Composable
fun MagNavHost(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    navOptions: NavOptions? = null
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier ) {
        homeScreen(
            onFabClick = {navController.navigateToSelect(navOptions)}
        )
        generateGraph(navController, navOptions)
    }
}