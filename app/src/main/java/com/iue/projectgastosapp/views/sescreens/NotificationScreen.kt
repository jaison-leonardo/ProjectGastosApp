package com.iue.projectgastosapp.views.sescreens

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.iue.projectgastosapp.enums.Frecuencias
import com.iue.projectgastosapp.room.PreferenceDatastore
import com.iue.projectgastosapp.room.PreferencesData
import com.iue.projectgastosapp.states.DataNotification
import com.iue.projectgastosapp.views.composable.CheckboxListRow
import com.iue.projectgastosapp.views.composable.OptionCheckItem
import com.iue.projectgastosapp.views.composable.PercentageTextField
import com.iue.projectgastosapp.views.composable.ShowDialog
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen() {
    val permissionState =
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
        )
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val dataNotification by remember { mutableStateOf(DataNotification()) }
    val preferencesData = PreferenceDatastore(context)
    LaunchedEffect(true) {
        val preferences = PreferencesData()
        preferencesData.saveData(preferences)

    }
    var checked by remember { mutableStateOf(dataNotification.notificacionActivada) }

    var umbralValue by remember { mutableStateOf(dataNotification.umbralMaxGasto) }

    val frecuencias = listOf(
        Frecuencias.DIARIA.label,
        Frecuencias.SEMANAL.label
    )
    val checkboxStates = remember { mutableStateListOf(false, false) }
    var checkItemSelect by remember { mutableStateOf("") }
    if (dataNotification.notificacionFrecuencia == frecuencias[0]) {
        checkboxStates[0] = true
        checkboxStates[1] = false
        checkItemSelect = frecuencias[0]
    } else {
        checkboxStates[0] = false
        checkboxStates[1] = true
        checkItemSelect = frecuencias[1]
    }

    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Text(text = "Configurar Notificaciones", fontWeight = FontWeight.Bold)
        Switch(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            checked = checked,
            onCheckedChange = {
                checked = it
                lifecycleOwner.lifecycleScope.launch {

                }
            }
        )
        if (checked) {
            LaunchedEffect(true) {
                permissionState.launchPermissionRequest()
            }
            if (permissionState.status.isGranted) {
                Spacer(modifier = Modifier.padding(15.dp))
                Text(text = "Umbral de gasto o limite maximo", fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 10.dp, end = 10.dp)
                ) {
                    PercentageTextField(
                        value = umbralValue
                    ) {
                        if (it.isDigitsOnly() && (it.toIntOrNull() ?: 0) <= 100) {
                            umbralValue = it
                        }
                    }
                    Text(
                        text = "%",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(30.dp))
                Text(text = "Frecuencia", fontWeight = FontWeight.Bold)
                val options = frecuencias.mapIndexed { index, frecuencia ->
                    val checkedFrecuencia = checkboxStates[index]
                    OptionCheckItem(
                        label = frecuencia,
                        checked = checkedFrecuencia,
                        onCheckedChange = {
                            if (!checkedFrecuencia) {
                                checkboxStates[index] = !checkedFrecuencia
                                checkboxStates[(index + 1) % 2] = !checkboxStates[(index + 1) % 2]
                            }
                            checkItemSelect = frecuencia
                        }
                    )
                }
                CheckboxListRow(options = options)
                Spacer(modifier = Modifier.size(30.dp))
                // Botón "Guardar Configuración"

                Button(
                    onClick = {
                        lifecycleOwner.lifecycleScope.launch {

                        }
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
            } else if (permissionState.status.shouldShowRationale) {
                message =
                    "Se necesita permisos para enviar notificaciones, " +
                            "para usar esta funcionalidad por favor acepte los permisos"
                showDialog = true
                permissionState.launchPermissionRequest()
            } else {
                checked = false
            }

        } else {
            Row {
                Text(text = "No hay notificaciones configuradas", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.size(5.dp))
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red
                )
            }

        }
    }
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    NotificationScreen()
}