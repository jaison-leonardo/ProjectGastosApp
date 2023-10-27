package com.iue.projectgastosapp.views.sescreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SecurityScreen() {
    var checked by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "Habilitar autenticación alternativa (Si esta disponible)",
            fontWeight = FontWeight.Bold
        )
        Switch(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
        if (checked) {
            Spacer(modifier = Modifier.padding(15.dp))
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
            ) {
                Row {
                    Text(text = "Guardar Configuración", color = Color.White)
                    Spacer(modifier = Modifier.size(5.dp))
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        } else {
            Row {
                Text(
                    text = "No hay alternativas de autenticación o no estan disponibles",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(5.dp))
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red
                )
            }

        }
    }
}