package com.ry05k2ulv.myapplication.ui.home

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onFabClick: () -> Unit
) {
    SideEffect {
        viewModel.refreshImageInfoList()
    }

    val imageInfoList = viewModel.imageInfoList.collectAsState().value

    var selectedImageUris by remember { mutableStateOf(setOf<Uri>()) }
    var selectMode by remember(selectedImageUris) { mutableStateOf(selectedImageUris.isNotEmpty()) }

    Box(Modifier.fillMaxSize()) {
        ImageGrid(
            imageUris = imageInfoList.map { it.uri },
            selectedImageUris = selectedImageUris,
            gridColumns = 2,
            onImageClick = { if (selectMode) selectedImageUris = selectedImageUris.toggle(it) },
            onImageLongClick = { selectedImageUris = selectedImageUris.toggle(it) ; Log.d("toggled", "$selectedImageUris")}
        )

        FloatingActionButton(
            onClick = if (selectMode) {
                {
                    viewModel.deleteImages(selectedImageUris)
                    selectedImageUris = setOf()
                }
            } else {
                onFabClick
            },
            Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            if (selectMode) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            } else {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}


@Composable
fun ImageGrid(
    imageUris: List<Uri>,
    selectedImageUris: Set<Uri>,
    gridColumns: Int,
    onImageClick: (Uri) -> Unit,
    onImageLongClick: (Uri) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        contentPadding = PaddingValues(2.dp),
    ) {
        items(imageUris) { uri ->
            ImageItem(
                uri = uri,
                selected = uri in selectedImageUris,
                onClick = { onImageClick(uri) },
                onLongClick = { onImageLongClick(uri) })
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

private fun Set<Uri>.toggle(uri: Uri) =
    if (uri in this) this - uri else this + uri