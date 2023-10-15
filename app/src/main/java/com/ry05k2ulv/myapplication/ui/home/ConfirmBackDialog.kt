package com.ry05k2ulv.myapplication.ui.home


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmBackDialog(
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
            Icon(imageVector = Icons.Default.Home, contentDescription = null)
        },
        title = {
            Text(
                text = "Back to Home Screen",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "Do you want to discard the inputs?",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}