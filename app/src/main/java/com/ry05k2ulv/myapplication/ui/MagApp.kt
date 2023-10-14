package com.ry05k2ulv.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ry05k2ulv.myapplication.navigation.MagNavHost
import com.ry05k2ulv.myapplication.ui.generate.select.SELECT_NAVIGATION_ROUTE
import com.ry05k2ulv.myapplication.ui.home.HOME_NAVIGATION_ROUTE
import com.ry05k2ulv.myapplication.ui.home.navigateToHome
import com.ry05k2ulv.myapplication.ui.settings.SettingsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagApp() {
    val navController = rememberNavController()
    val navOptions =
        NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(HOME_NAVIGATION_ROUTE, false)
            .build()

    val current = navController.currentBackStackEntryAsState().value?.destination?.route
        ?: HOME_NAVIGATION_ROUTE

    var showSettingsDialog by remember { mutableStateOf(false) }

    if (showSettingsDialog) {
        SettingsDialog(onDismiss = { showSettingsDialog = false })
    }

    Scaffold(
        topBar = {
            MagTopAppBar(
                title = current.uppercase(),
                navigationIcon = Icons.Default.Home,
                navigationIconDescription = "Home",
                onNavigationIconClick = { navController.navigateToHome(navOptions) },
                actionIcon = Icons.Default.Settings,
                actionIconDescription = "Settings",
                onAction = { showSettingsDialog = true }
            )
        }
    ) {
        MagNavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = HOME_NAVIGATION_ROUTE,
            navOptions = navOptions
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagTopAppBar(
    title: String,
    navigationIcon: ImageVector,
    navigationIconDescription: String,
    onNavigationIconClick: () -> Unit,
    actionIcon: ImageVector,
    actionIconDescription: String,
    onAction: () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    CenterAlignedTopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = navigationIcon, contentDescription = navigationIconDescription)
            }
        },
        actions = {
            IconButton(onClick = onAction) {
                Icon(imageVector = actionIcon, contentDescription = actionIconDescription)
            }
        }, colors = colors
    )
}