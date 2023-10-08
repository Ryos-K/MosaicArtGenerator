package com.ry05k2ulv.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.rememberNavController
import com.ry05k2ulv.myapplication.navigation.MagNavHost
import com.ry05k2ulv.myapplication.ui.generate.GENERATE_NAVIGATION_ROUTE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            MagTopAppBar(
                title = "Select Images",
                actionIcon = Icons.Default.Settings,
                actionIconDescription = "Settings",
                onAction = {}
            )
        }
    ) {
        MagNavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = GENERATE_NAVIGATION_ROUTE
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagTopAppBar(
    title: String,
    actionIcon: ImageVector,
    actionIconDescription: String,
    onAction: () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    TopAppBar(
        title = {
            Text(title)
        },
        actions = {
            IconButton(onClick = onAction) {
                Icon(imageVector = actionIcon, contentDescription = actionIconDescription)
            }
        }, colors = colors
    )
}