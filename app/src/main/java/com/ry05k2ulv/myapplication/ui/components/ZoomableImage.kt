package com.ry05k2ulv.myapplication.ui.components

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.ry05k2ulv.myapplication.ui.theme.MosaicArtGeneratorTheme

@Stable
class ZoomState(private val maxScale: Float) {
    private var _scale = mutableFloatStateOf(1f)
    val scale: Float
        get() = _scale.floatValue

    private var _translationX = mutableFloatStateOf(0f)
    val translationX: Float
        get() = _translationX.floatValue

    private var _translationY = mutableFloatStateOf(0f)
    val translationY: Float
        get() = _translationY.floatValue

    private var layoutSize = Size.Zero
    fun setLayoutSize(size: Size) {
        layoutSize = size
    }

    fun applyGesture(centroid: Offset, pan: Offset, zoom: Float) {
        _scale.floatValue = (scale * zoom).coerceIn(1f, maxScale)
        val deltaX = pan.x +
                if (scale == maxScale) 0f
                else (translationX - (centroid.x - layoutSize.width / 2)) * (zoom - 1)
        val deltaY = pan.y +
                if (scale == maxScale) 0f
                else (translationY - (centroid.y - layoutSize.height / 2)) * (zoom - 1)
        val boundsX = (layoutSize.width * (scale - 1) / 2)
        val boundsY = (layoutSize.height * (scale - 1) / 2)
        _translationX.floatValue = (translationX + deltaX).coerceIn(-boundsX, boundsX)
        _translationY.floatValue = (translationY + deltaY).coerceIn(-boundsY, boundsY)
    }
}

@Composable
fun rememberZoomState() = remember { ZoomState(8f) }

@Composable
fun ZoomableImage(
    modifier: Modifier,
    bitmap: ImageBitmap,
    contentDescription: String?,
    zoomState: ZoomState = rememberZoomState()
) {
    Image(
        bitmap = bitmap,
        contentDescription = contentDescription,
        modifier = modifier
            .onSizeChanged { size -> zoomState.setLayoutSize(size.toSize()) }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    zoomState.applyGesture(centroid, pan, zoom)
                }
            }
            .graphicsLayer(
                scaleX = zoomState.scale,
                scaleY = zoomState.scale,
                translationX = zoomState.translationX,
                translationY = zoomState.translationY
            )
    )
}

@Preview
@Composable
fun ZoomableImagePreview() {
    MosaicArtGeneratorTheme {
        val (w, h) = 1080 to 810
        val pixels = IntArray(w * h) { it or 0xFF000000.toInt() }
        val bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888)

        Surface {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Green)
            ) {
                ZoomableImage(
                    modifier = Modifier.align(Alignment.Center),
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = ""
                )
            }
        }
    }
}