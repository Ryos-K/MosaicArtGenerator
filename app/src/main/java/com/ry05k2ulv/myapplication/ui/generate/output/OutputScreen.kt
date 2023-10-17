package com.ry05k2ulv.myapplication.ui.generate.output

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.IncompleteCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputScreen(
    viewModel: ResultViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val result = uiState.result
    val progress = uiState.progress
    val running = uiState.running
    val saved = uiState.uriSaved

//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            // Open camera
//        } else {
//            // Show dialog
//        }
//    }

    Box(Modifier.fillMaxSize()) {
        if (result != null)
            Image(
                bitmap = result.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )

        ProgressSection(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            progress = progress
        )

        OperationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            onShareClick = {
//                if (ContextCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    return@OperationBar
//                }

                val uri = viewModel.saveResultExternal()
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    type = "image/png"
                }
                context.startActivity(sendIntent)
            },
            onSaveClick = { viewModel.saveResult("mag") },
            saved = saved,
            enabled = !running
        )
    }
}

@Composable
private fun ProgressSection(modifier: Modifier, progress: Float) {
    val animateProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(dampingRatio = 2f),
        label = ""
    )
    Column(modifier) {
        ProgressText(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            progress = animateProgress
        )
        GradientProgressBar(
            progress = animateProgress, modifier = Modifier
                .padding(16.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ProgressText(
    modifier: Modifier,
    progress: Float
) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(
            "Progress :",
            Modifier.padding(4.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "${(progress * 100).toInt()} (%)",
            Modifier.padding(4.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun GradientProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
        .height(4.dp)
        .fillMaxWidth(0.8f),
    brush: Brush = Brush.horizontalGradient(
        listOf(
            Color.Yellow,
            Color.Green
        )
    ),
    background: Color = Color.Gray
) {
    Box(modifier.background(brush), contentAlignment = Alignment.CenterEnd) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth((1 - progress))
                .background(background)
        )
    }
}

@Composable
private fun OperationBar(
    modifier: Modifier,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
    enabled: Boolean,
    saved: SaveState
) {
    Row(modifier) {
        ShareButton(onShareClick = onShareClick, modifier = Modifier.weight(1f), enabled = enabled)
        SaveButton(onSaveClick = onSaveClick, modifier = Modifier.weight(1f), saved = saved, enabled = enabled)
    }
}

@Composable
private fun ShareButton(
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    IconButton(onClick = onShareClick, modifier.height(64.dp), enabled) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Image"
            )
            Text("Share")
        }
    }
}

@Composable
private fun SaveButton(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    saved: SaveState,
    enabled: Boolean = true
) {
    IconButton(onClick = onSaveClick, modifier.height(64.dp), enabled && saved == SaveState.YET) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (saved) {
                SaveState.YET -> {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save Image"
                    )
                    Text(text = "Save")
                }
                SaveState.SAVING -> {
                    Icon(
                        imageVector = Icons.Default.IncompleteCircle,
                        contentDescription = "Save Image"
                    )
                    Text(text = "Saving")
                }
                SaveState.SAVED -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Saved"
                    )
                    Text(text = "Saved")
                }
            }
        }
    }
}