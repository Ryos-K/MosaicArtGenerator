package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SelectScreen(
    viewModel: SelectViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    val targetImageUri = viewModel.targetImageUri.value
    val materialImageUriSet = viewModel.materialImageUriSet.value
    val gridSize = viewModel.gridSize.intValue

    val targetImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateTargetImageUri(uri)
    }

    val materialImageLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) { uris: List<Uri> ->
            viewModel.addMaterialImageUri(uris)
        }

    var current by remember { mutableStateOf<SelectRoute>(SelectRoute.SelectTarget) }

    val progress by animateFloatAsState(
        when (current) {
            SelectRoute.SelectTarget -> 0f
            SelectRoute.SelectMaterial -> 0.5f
            SelectRoute.Confirm -> 1f
        }, label = "Progress"
    )

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(4.dp)
        )

        when (current) {
            SelectRoute.SelectTarget -> SelectTargetScreen(
                modifier = Modifier.weight(1f),
                uri = targetImageUri,
                gridSize = gridSize,
                cropRect = IntRect(0, 0, 0, 0),
                onClickSelect = { targetImageLauncher.launch("image/*") },
                onSlide = {},
                onMoveRect = {}
            )

            SelectRoute.SelectMaterial -> SelectMaterialScreen(
                modifier = Modifier.weight(1f),
                uriSet = materialImageUriSet,
                onClickSelect = { materialImageLauncher.launch("image/*") }
            )

            SelectRoute.Confirm -> ConfirmScreen(
                modifier = Modifier.weight(1f),
                targetUri = targetImageUri,
                gridSize = gridSize,
                materialUriSet = materialImageUriSet
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    when (current) {
                        SelectRoute.SelectTarget -> onBack()
                        SelectRoute.SelectMaterial -> current = SelectRoute.SelectTarget
                        SelectRoute.Confirm -> current = SelectRoute.SelectMaterial
                    }
                },
                Modifier.padding(16.dp, 0.dp),
            ) {
                Icon(imageVector = Icons.Default.NavigateBefore, contentDescription = null)
                Text(text = "Back", Modifier)
            }
            TextButton(
                onClick = {
                    when (current) {
                        SelectRoute.SelectTarget -> current = SelectRoute.SelectMaterial
                        SelectRoute.SelectMaterial -> current = SelectRoute.Confirm
                        SelectRoute.Confirm -> onNext()
                    }
                },
                Modifier.padding(16.dp, 0.dp)
            ) {
                Text(text = "Next")
                Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null)
            }
        }
    }
}

enum class SelectRoute(val no: Int, val title: String) {
    SelectTarget(1, "Select Target Image"),
    SelectMaterial(2, "Select Material Image"),
    Confirm(3, "Confirm Selection")
}