package com.ry05k2ulv.myapplication.ui.generate.result

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ResultScreen(
    viewModel: ResultViewModel = hiltViewModel()
) {
    val result = viewModel.result.collectAsState().value

    if (result == null) {
        Text("Not Found")
    } else {
        Text("Yes")
        Image(bitmap = result.asImageBitmap(), contentDescription = null)
    }
}