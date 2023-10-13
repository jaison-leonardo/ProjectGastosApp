package com.iue.projectgastosapp.views.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShowDialog(show: Boolean, message: String, onDismiss: () -> Unit, onButtonClick: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onButtonClick() }) {
                    Text("Aceptar")
                }
            },
            title = { Text("") },
            text = { Text(message) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
        )
    }
}