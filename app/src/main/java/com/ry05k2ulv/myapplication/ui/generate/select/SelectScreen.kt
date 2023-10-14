package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SelectScreen(
    viewModel: SelectViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNext: (Uri, Set<Uri>, Int) -> Unit,
) {
    val targetImageUri = viewModel.targetImageUri.value
    val selectMaterialUiState = viewModel.materialUiState.collectAsState().value
    val gridSize = viewModel.gridSize.intValue

    var current by remember { mutableStateOf(SelectRoute.SelectTarget) }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (current) {
            SelectRoute.SelectTarget -> SelectTargetScreen(
                modifier = Modifier.weight(1f),
                uri = targetImageUri,
                gridSize = gridSize,
                cropRect = IntRect(0, 0, 0, 0),
                onSlide = {},
                onMoveRect = {},
                updateTargetImageUri = viewModel::updateTargetImageUri
            )

            SelectRoute.SelectMaterial -> SelectMaterialScreen(
                modifier = Modifier.weight(1f),
                uiState = selectMaterialUiState,
                addMaterials = viewModel::addMaterials,
                removeMaterials = viewModel::removeMaterials
            )
        }

        BottomBar(
            modifier = Modifier.height(56.dp),
            showBackButton = current != SelectRoute.SelectTarget,
            onBack = {
                when (current) {
                    SelectRoute.SelectTarget -> {}
                    SelectRoute.SelectMaterial -> current = SelectRoute.SelectTarget
                }
            },
            onNext = {
                when (current) {
                    SelectRoute.SelectTarget -> current = SelectRoute.SelectMaterial
                    SelectRoute.SelectMaterial -> {
                        if (viewModel.isReady()) onNext(
                            targetImageUri!!, selectMaterialUiState.imageUriSet, gridSize
                        )
                    }
                }
            },
            nextButtonText = when (current) {
                SelectRoute.SelectTarget -> "Next"
                SelectRoute.SelectMaterial -> "Finish"
            }
        )
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier,
    showBackButton: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    backButtonText: String = "Back",
    nextButtonText: String = "Next"
) {
    Box(
        modifier = modifier,
    ) {
        if (showBackButton) {
            BackButton(
                text = backButtonText,
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        NextButton(
            text = nextButtonText,
            onClick = onNext,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }

}

@Composable
private fun BackButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(imageVector = Icons.Default.NavigateBefore, contentDescription = "Back")
        Text(text = text)
    }
}

@Composable
private fun NextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = "Next")
    }
}

enum class SelectRoute {
    SelectTarget,
    SelectMaterial,
}