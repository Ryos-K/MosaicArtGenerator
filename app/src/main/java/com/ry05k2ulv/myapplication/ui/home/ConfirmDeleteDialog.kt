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
import androidx.compose.ui.res.stringResource
import com.ry05k2ulv.myapplication.R
import com.ry05k2ulv.myapplication.R.*


@Composable
fun ConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(text = stringResource(string.home_confirm_delete_dialog_yes), style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(string.home_confirm_delete_dialog_no), style = MaterialTheme.typography.labelLarge)
            }
        },
        icon = {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        },
        title = {
            Text(
                text = stringResource(string.home_confirm_delete_dialog_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = stringResource(string.home_confirm_delete_dialog_message),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}