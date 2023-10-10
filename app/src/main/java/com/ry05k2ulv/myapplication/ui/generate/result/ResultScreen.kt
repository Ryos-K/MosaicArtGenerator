package com.ry05k2ulv.myapplication.ui.generate.result

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ry05k2ulv.myapplication.ui.generate.GenerateViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResultScreen(
    viewModel: GenerateViewModel = hiltViewModel()
) {
    val result = viewModel.result.collectAsState().value

    if (result == null) {
        Text("Not Found")
    } else {
        Text("Yes")
        Image(bitmap = result.asImageBitmap(), contentDescription = null)
    }
}