package com.ry05k2ulv.myapplication.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun ConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(text = "Yes", style = MaterialTheme.typography.bodyLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "No")
            }
        },
        icon = {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        },
        title = {
            Text(
                text = "Delete Images",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "Do you want to delete images?",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}