package com.ry05k2ulv.myapplication.ui.generate.input

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ry05k2ulv.myapplication.ui.LocalSnackbarHostState
import kotlinx.coroutines.launch

@Composable
fun InputScreen(
    viewModel: InputViewModel = hiltViewModel(),
    onFinish: (Uri, Set<Uri>, Int, Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    val targetUiState = viewModel.targetUiState.collectAsState().value
    val materialUiState = viewModel.materialUiState.collectAsState().value

    val snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current

    var current by remember { mutableStateOf(InputRoute.InputTarget) }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (current) {
            InputRoute.InputTarget -> InputTargetScreen(
                modifier = Modifier.weight(1f),
                uiState = targetUiState,
                onGridSizeChanged = viewModel::updateGridSize,
                onOutputSizeChanged = viewModel::updateOutputSize,
                updateTargetImageUri = viewModel::updateTargetImageUri,
            )

            InputRoute.InputMaterial -> InputMaterialScreen(
                modifier = Modifier.weight(1f),
                uiState = materialUiState,
                addMaterials = viewModel::addMaterials,
                removeMaterials = viewModel::removeMaterials
            )
        }

        BottomBar(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            title = current.title,
            showBackButton = current != InputRoute.InputTarget,
            onBack = {
                when (current) {
                    InputRoute.InputTarget -> {}
                    InputRoute.InputMaterial -> current = InputRoute.InputTarget
                }
            },
            onNext = {
                when (current) {
                    InputRoute.InputTarget -> current = InputRoute.InputMaterial
                    InputRoute.InputMaterial -> {
                        when {
                            targetUiState.imageUri == null -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Target Image is not selected.",
                                        null,
                                        true
                                    )
                                }
                            }

                            materialUiState.imageUriSet.isEmpty() -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Material Image is not selected.",
                                        null,
                                        true
                                    )
                                }
                            }

                            else -> {
                                onFinish(
                                    targetUiState.imageUri!!,
                                    materialUiState.imageUriSet,
                                    targetUiState.gridSize,
                                    targetUiState.outputSize
                                )
                            }
                        }

                    }
                }
            },
            nextButtonText = when (current) {
                InputRoute.InputTarget -> "Next"
                InputRoute.InputMaterial -> "Finish"
            }
        )
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier,
    title: String,
    showBackButton: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    backButtonText: String = "Back",
    nextButtonText: String = "Next"
) {
    val background = MaterialTheme.colorScheme.secondaryContainer
    val textColor = MaterialTheme.colorScheme.onSecondaryContainer
    Box(
        modifier = modifier.background(background),
    ) {
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = textColor.copy(alpha = 0.7f)
        )
        if (showBackButton) {
            BackButton(
                text = backButtonText,
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart),
                color = textColor
            )
        }
        NextButton(
            text = nextButtonText,
            onClick = onNext,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = textColor
        )
    }

}

@Composable
private fun BackButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(imageVector = Icons.Default.NavigateBefore, contentDescription = "Back", tint = color)
        Text(text = text, color = color)
    }
}

@Composable
private fun NextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text, color = color)
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = "Next", tint = color)
    }
}

enum class InputRoute(val title: String) {
    InputTarget("Input Target Image"),
    InputMaterial("Input Material Images"),
}