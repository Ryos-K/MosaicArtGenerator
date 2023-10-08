package com.ry05k2ulv.myapplication.ui.generate.result

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ry05k2ulv.myapplication.ui.generate.GenerateViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResultScreen(
    viewModel: GenerateViewModel = hiltViewModel()
) {
    val target = viewModel.targetImageUri.value
    Log.d("target", target.toString())
    AsyncImage(model = target, contentDescription = "")
    
//    val bitmap = viewModel.getBitmap()
//    if (bitmap == null) Log.d(null, "bitmap is null")
//    bitmap?.let { Image(bitmap = bitmap.asImageBitmap(), contentDescription = "") }
}