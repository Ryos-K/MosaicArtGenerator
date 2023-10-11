package com.ry05k2ulv.myapplication.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onFabClick: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        FloatingActionButton(onClick = onFabClick,
            Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}