package com.ry05k2ulv.myapplication.ui.generate.result

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: ResultViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val result = viewModel.result.collectAsState().value
    val progress = viewModel.progress.value
    val running = viewModel.running.value

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Box(Modifier.fillMaxSize()) {
        ProgressSection(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp), progress = progress
        )
        if (result != null)
            Image(
                bitmap = result.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )

        OperationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            onShareClick = { /*TODO*/ },
            onSaveClick = { showBottomSheet = true },
            enabled = !running
        )
    }
    SaveBottomSheet(
        onDismiss = { showBottomSheet = false },
        onSave = {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion { showBottomSheet = false }
            viewModel.saveResult(it);
        },
        sheetState = sheetState,
        shouldShow = showBottomSheet
    )
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
    enabled: Boolean
) {
    Row(modifier) {
        ShareButton(onShareClick = onShareClick, modifier = Modifier.weight(1f), enabled = enabled)
        SaveButton(onSaveClick = onSaveClick, modifier = Modifier.weight(1f), enabled = enabled)
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
    enabled: Boolean = true
) {
    IconButton(onClick = onSaveClick, modifier.height(64.dp), enabled) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save Image"
            )
            Text(text = "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    sheetState: SheetState,
    shouldShow: Boolean = false,
) {
    if (!shouldShow) return

    var filename by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Text("Save Image", style = MaterialTheme.typography.titleLarge)
        TextField(value = filename, onValueChange = { filename = it })
        Button(onClick = {
            if (filename != "")
                onSave(filename)
        }
        ) {
            Text(text = "Save")
        }
    }
}