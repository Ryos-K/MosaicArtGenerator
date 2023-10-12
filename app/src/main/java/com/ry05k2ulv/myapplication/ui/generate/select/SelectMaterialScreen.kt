package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

const val INITIAL_GRID_COLUMNS = 3
const val MIN_GRID_COLUMNS = 2
const val MAX_GRID_COLUMNS = 4

@Composable
fun SelectMaterialScreen(
    modifier: Modifier,
    uiState: SelectMaterialUiState,
    addMaterials: (List<Uri>) -> Unit,
    removeMaterials: (Set<Uri>) -> Unit
) {
    val materialImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        addMaterials(uris)
    }

    var selectedImageUris by remember { mutableStateOf(setOf<Uri>()) }
    var selectMode by remember(selectedImageUris) { mutableStateOf(selectedImageUris.isNotEmpty()) }
    var gridColNum by remember { mutableIntStateOf(INITIAL_GRID_COLUMNS) }

    val uris = uiState.imageUriSet

    Column(
        modifier
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColNum),
            Modifier
                .weight(1f),
            contentPadding = PaddingValues(2.dp)
        ) {
            items(uris.toList()) { uri ->
                ImageItem(
                    uri = uri,
                    selected = uri in selectedImageUris,
                    onClick = {
                        if (selectMode) {
                            selectedImageUris = selectedImageUris.toggle(uri)
                        } else {

                        }
                    },
                    onLongClick = {
                        selectedImageUris = selectedImageUris.toggle(uri)
                    })
            }
        }

        OperationBar(
            selectMode = selectMode,
            onPictureClick = { materialImageLauncher.launch("image/*") },
            onSelectionToggle = { selectedImageUris = if (selectMode) setOf() else uris.toSet() },
            onRemoveClick = {
                removeMaterials(selectedImageUris)
                selectedImageUris = setOf()
            },
            onGridSwitch = { gridColNum = when(gridColNum) {
                in MIN_GRID_COLUMNS until MAX_GRID_COLUMNS -> gridColNum + 1
                MAX_GRID_COLUMNS -> MIN_GRID_COLUMNS
                else -> INITIAL_GRID_COLUMNS
            } }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageItem(
    uri: Uri,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val contentPadding by animateDpAsState(
        targetValue = if (selected) 4.dp else 2.dp, label = "padding"
    )
    val selectColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
    Box(
        Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .padding(contentPadding)
            .border(2.dp, selectColor, RoundedCornerShape(4.dp))
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp))
        )
        if (selected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "check",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                tint = selectColor
            )
        }
    }
}

@Composable
private fun OperationBar(
    selectMode: Boolean,
    onPictureClick: () -> Unit,
    onSelectionToggle: () -> Unit,
    onRemoveClick: () -> Unit,
    onGridSwitch: () -> Unit
) {
    val itemModifier = Modifier.padding(8.dp)
    Row(
        Modifier
            .height(64.dp)
            .fillMaxWidth()
    ) {
        PictureButton(onClick = onPictureClick, itemModifier)
        SelectionToggleButton(onClick = onSelectionToggle, selectMode = selectMode, itemModifier)
        RemoveButton(onClick = onRemoveClick, selectMode = selectMode, itemModifier)
        Spacer(modifier = Modifier.weight(1f))
        GridSwitchButton(onClick = onGridSwitch, itemModifier)
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
private fun SelectionToggleButton(
    onClick: () -> Unit,
    selectMode: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (selectMode) MaterialTheme.colorScheme.primary else Color.Gray
    IconButton(onClick = onClick, modifier) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Toggle Select",
            tint = color
        )
    }
}

@Composable
private fun RemoveButton(
    onClick: () -> Unit,
    selectMode: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = selectMode,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = selectMode
        ) {
            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Remove Images")
        }
    }
}

@Composable
private fun GridSwitchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier) {
        Icon(
            imageVector = Icons.Default.GridView,
            contentDescription = "Switch the number of grid column"
        )
    }
}

private fun Set<Uri>.toggle(uri: Uri) =
    if (uri in this) this - uri else this + uri
