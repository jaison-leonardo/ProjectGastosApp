package com.iue.projectgastosapp.views.sescreens
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.views.composable.IndeterminateCircularIndicator

@Composable
fun BackupScreen() {
    var isLoading by remember { mutableStateOf(false) }
    Column {
        Text(text = "Crear Copia de seguridad", fontWeight = FontWeight.Bold)
        Button(
            onClick = {
                isLoading = !isLoading
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
        ) {
            Row {
                Text(text = "Comenzar", color = Color.White)
                Spacer(modifier = Modifier.size(5.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(20.dp))
//                LoadingAnimation()
                IndeterminateCircularIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BackupScreenPreview() {
    BackupScreen()
}