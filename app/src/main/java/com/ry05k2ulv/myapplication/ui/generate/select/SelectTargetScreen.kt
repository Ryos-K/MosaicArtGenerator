package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlin.math.roundToInt

@Composable
internal fun SelectTargetScreen(
    modifier: Modifier,
    uri: Uri?,
    gridSize: Int,
    cropRect: IntRect,
    onSlide: (Int) -> Unit,
    onMoveRect: (IntRect) -> Unit,
    updateTargetImageUri: (Uri?) -> Unit
) {
    val targetImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        updateTargetImageUri(uri)
    }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth(0.8f)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (uri == null) {
                TextButton(onClick = { targetImageLauncher.launch("image/*") }) {
                    Text("Select Image")
                }
            } else {
                Image(painter = rememberAsyncImagePainter(uri), contentDescription = "Image")
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Grid Size : $gridSize")

        Slider(value = gridSize.toFloat(), onValueChange = { onSlide(it.roundToInt()) })

    }
}

@Composable
private fun ImageCropCanvas(

) {
    Canvas(Modifier) {

    }
}