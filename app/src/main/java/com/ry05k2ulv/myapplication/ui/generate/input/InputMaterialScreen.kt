package com.ry05k2ulv.myapplication.ui.generate.input

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
fun InputMaterialScreen(
    modifier: Modifier,
    uiState: MaterialUiState,
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
    var gridColumns by remember { mutableIntStateOf(INITIAL_GRID_COLUMNS) }


    val imageUris = uiState.imageUriSet

    Box(
        modifier
    ) {
        ImageGrid(
            imageUris = imageUris,
            selectedImageUris = selectedImageUris,
            gridColumns = gridColumns,
            onImageClick = {
                if (selectMode) {
                    selectedImageUris = selectedImageUris.toggle(it)
                } else {

                }
            },
            onImageLongClick = {selectedImageUris = selectedImageUris.toggle(it)}
        )

        OperationBar(
            modifier = Modifier.align(Alignment.TopCenter),
            selectMode = selectMode,
            onPictureClick = { materialImageLauncher.launch("image/*") },
            onSelectionToggle = {
                selectedImageUris = if (selectMode) setOf() else imageUris.toSet()
            },
            onRemoveClick = {
                removeMaterials(selectedImageUris)
                selectedImageUris = setOf()
            },
            onGridSwitch = {
                gridColumns = when (gridColumns) {
                    in MIN_GRID_COLUMNS until MAX_GRID_COLUMNS -> gridColumns + 1
                    MAX_GRID_COLUMNS -> MIN_GRID_COLUMNS
                    else -> INITIAL_GRID_COLUMNS
                }
            }
        )

    }
}

@Composable
fun ImageGrid(
    imageUris: Set<Uri>,
    selectedImageUris: Set<Uri>,
    gridColumns: Int,
    onImageClick: (Uri) -> Unit,
    onImageLongClick: (Uri) -> Unit
    ) {
    val lazyGridState = rememberLazyGridState()
    val showOperationBar by remember(lazyGridState) {
        derivedStateOf { lazyGridState.firstVisibleItemScrollOffset == 0 }
    }
    val space by animateDpAsState(targetValue =  if (showOperationBar) 48.dp else 0.dp,  label = "space")
    Column(
        Modifier.fillMaxSize(),
    ) {

            Spacer(Modifier.height(space))

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns),
            contentPadding = PaddingValues(2.dp),
            state = lazyGridState
        ) {
            items(imageUris.toList()) { uri ->
                ImageItem(
                    uri = uri,
                    selected = uri in selectedImageUris,
                    onClick = {onImageClick(uri)},
                    onLongClick = {onImageLongClick(uri)})
            }
        }
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
    modifier: Modifier = Modifier,
    selectMode: Boolean,
    onPictureClick: () -> Unit,
    onSelectionToggle: () -> Unit,
    onRemoveClick: () -> Unit,
    onGridSwitch: () -> Unit,
) {
    val itemModifier = Modifier
        .padding(8.dp)
    Row(
        modifier
            .animateContentSize(animationSpec = spring(dampingRatio = 2f))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f))
            .height(48.dp)
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
