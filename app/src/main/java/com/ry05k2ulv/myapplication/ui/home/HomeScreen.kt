package com.ry05k2ulv.myapplication.ui.home

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
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ry05k2ulv.myapplication.imagestore.ImageInfo

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onFabClick: () -> Unit
) {
    val imageInfoList = viewModel.imageInfoList.collectAsState().value

    Box(Modifier.fillMaxSize()) {
        val rowNum = 2
        LazyVerticalGrid(
            columns = GridCells.Fixed(rowNum),
            contentPadding = PaddingValues(2.dp)
        ) {

            items(imageInfoList) { imageInfo ->
                ImageInfoCard(modifier = Modifier.padding(4.dp), imageInfo = imageInfo)
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun ImageInfoCard(
    modifier: Modifier,
    imageInfo: ImageInfo
) {
    Card(
        modifier
    ) {
        AsyncImage(
            model = imageInfo.uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1f)
        )
        Text(text = imageInfo.filename)
    }
}