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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ResultScreen(
    viewModel: ResultViewModel = hiltViewModel()
) {
    val result = viewModel.result.collectAsState().value
    val progress = viewModel.progress.value

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        ProgressText(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), progress = progress
        )
        GradientProgressBar(
            progress = progress, modifier = Modifier
                .padding(16.dp)
                .height(4.dp)
                .fillMaxWidth()
        )
        if (result != null)
            Image(bitmap = result.asImageBitmap(), contentDescription = null)
    }
}

@Composable
fun ProgressText(
    modifier: Modifier,
    progress: Float
) {
    val progressPercent = (progress * 100).toInt()
    Row(modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(
            "Progress :",
            Modifier.padding(4.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "$progressPercent (%)",
            Modifier.padding(4.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun GradientProgressBar(
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
    val animateProgress by animateFloatAsState(targetValue = (1 - progress), animationSpec = spring(dampingRatio = 2f), label = "")
    Box(modifier.background(brush), contentAlignment = Alignment.CenterEnd) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth((animateProgress))
                .background(background)
        )
    }
}