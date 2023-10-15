package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ry05k2ulv.myapplication.generator.MosaicArtGenerator
import kotlin.math.roundToInt

@Composable
internal fun SelectTargetScreen(
    modifier: Modifier,
    uiState: TargetUiState,
    onGridSizeChanged: (Int) -> Unit,
    updateTargetImageUri: (Uri?) -> Unit
) {
    val targetImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        updateTargetImageUri(uri)
    }

    val uri = uiState.imageUri
    val gridSize = uiState.gridSize

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        OperationBar(
            onPictureClick = { targetImageLauncher.launch("image/*") }
        )
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Image",
                modifier = Modifier.fillMaxHeight(0.7f),
                contentScale = ContentScale.Fit
            )

            AdvancedConfigurationCard(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(8.dp, 16.dp),
                gridSize = gridSize,
                onGridSizeChanged = onGridSizeChanged,
            )
        }
    }
}


@Composable
private fun OperationBar(
    modifier: Modifier = Modifier,
    onPictureClick: () -> Unit,
) {
    val itemModifier = Modifier
        .padding(8.dp)
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.65f))
            .height(48.dp)
    ) {
        PictureButton(onClick = onPictureClick, itemModifier)
    }
}

@Composable
private fun PictureButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier) {
        Icon(
            imageVector = Icons.Default.AddPhotoAlternate,
            contentDescription = "Add photo"
        )
    }
}

@Composable
private fun AdvancedConfigurationCard(
    modifier: Modifier,
    gridSize: Int,
    onGridSizeChanged: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp, 4.dp)
                .fillMaxWidth()
        ) {
            if (expanded) Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            else Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
            Text(
                text = "Advanced Configuration",
                modifier = Modifier.padding(4.dp, 0.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (expanded) {
            Divider(Modifier.padding(16.dp, 2.dp))
            Text(
                text = "Grid Size : $gridSize",
                modifier = Modifier.padding(24.dp, 4.dp),
                style = MaterialTheme.typography.titleSmall
            )
            Slider(
                value = gridSize.toFloat() / MosaicArtGenerator.UNIT_SIZE,
                onValueChange = { onGridSizeChanged(it.roundToInt() * MosaicArtGenerator.UNIT_SIZE) },
                modifier = Modifier.padding(16.dp, 4.dp),
                valueRange = with(MosaicArtGenerator) { MIN_UNIT_PER_GRID.toFloat()..MAX_UNIT_PER_GRID.toFloat() },
                steps = with(MosaicArtGenerator) { MAX_UNIT_PER_GRID - MIN_UNIT_PER_GRID }
            )
        }
    }
}