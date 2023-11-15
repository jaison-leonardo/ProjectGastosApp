package com.iue.projectgastosapp.views.sescreens

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.iue.projectgastosapp.room.PreferenceDatastore
import com.iue.projectgastosapp.views.composable.ShowDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SecurityScreen() {
    var checked by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preferencesData = PreferenceDatastore(context)
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    LaunchedEffect(true) {
        preferencesData.getData().collect {
            withContext(Dispatchers.Main) {
                Log.d("SplashScreen_data", it.toString())
                checked = it.authAlternativa
            }

        }
    }
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
                showDialog = false
                checked = it
                lifecycleOwner.lifecycleScope.launch {
                    preferencesData.getData().collect {
                        withContext(Dispatchers.Main) {
                            Log.d("SplashScreen_data", it.toString())
                            it.authAlternativa = checked
                            preferencesData.saveData(it)
                        }
                    }
                }
            }
        )
        if (checked) {
            Spacer(modifier = Modifier.padding(15.dp))
            Button(
                onClick = {
                    showDialog = true
                    message = "Configuración guardada"
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
            ShowDialog(
                show = showDialog,
                message = message,
                onDismiss = { showDialog = false },
                onButtonClick = { showDialog = false }
            )
        } else {
            Row {
                Text(
                    text = "Las alternativas de autenticación estan desactivadas",
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