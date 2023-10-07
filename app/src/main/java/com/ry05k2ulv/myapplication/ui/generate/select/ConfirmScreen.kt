package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
internal fun ConfirmScreen(
    modifier: Modifier,
    targetUri: Uri?,
    gridSize: Int,
    materialUriSet: Set<Uri>
) {
    Column(modifier) {
        TargetImageCard(uri = targetUri, gridSize = gridSize)
        MaterialImagesCard(uriSet = materialUriSet)
    }
}

@Composable
private fun TargetImageCard(
    uri: Uri?,
    gridSize: Int
) {
    var opened by remember { mutableStateOf(true) }
    Card(
        Modifier.animateContentSize()
    ) {
        Row {
            Text("Target Image Info", Modifier.weight(1f))
            IconButton(onClick = { opened = !opened }) {
                Icon(
                    imageVector = if (opened) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Open State"
                )
            }
        }
        if (opened) {
            Divider()
            Row {
                if (uri == null) {
                    Box(
                        Modifier
                            .aspectRatio(1f)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("NO SELECT")
                    }
                } else {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Target",
                        Modifier
                            .aspectRatio(1f)
                            .weight(1f)
                    )
                }

                Column(Modifier.weight(1f)) {
                    Text(text = "Grid Size : $gridSize")
                }
            }
        }
    }
}

@Composable
private fun MaterialImagesCard(
    uriSet: Set<Uri>
) {

    Card(Modifier) {
        Row {
            Text(text = "Material Images", Modifier.weight(1f))
        }
        Divider()

        val rowNum = 3
        LazyVerticalGrid(
            columns = GridCells.Fixed(rowNum),
            Modifier
                .weight(1f),
            contentPadding = PaddingValues(2.dp)
        ) {
            items(uriSet.toList()) { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }
    }
}