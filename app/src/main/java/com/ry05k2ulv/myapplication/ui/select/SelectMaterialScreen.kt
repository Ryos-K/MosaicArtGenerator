package com.ry05k2ulv.myapplication.ui.select

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun SelectMaterialScreen(
    modifier: Modifier,
    uriSet: Set<Uri>,
    onClickSelect: () -> Unit
) {
    Column(
        modifier
    ) {
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


        Button(onClick = onClickSelect) {
            Text("Select Material Image", style = MaterialTheme.typography.labelLarge)
        }
    }
}