package com.iue.projectgastosapp.views.sescreens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.enums.Frecuencias
import com.iue.projectgastosapp.views.composable.CheckboxListRow
import com.iue.projectgastosapp.views.composable.OptionCheckItem
import me.thekusch.composeview.CurrencyTextField
import java.util.Locale

@Composable
fun NotificationScreen() {
    var checked by remember { mutableStateOf(false) }
    var umbralValue by remember { mutableStateOf("") }

    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val frecuencias = listOf(
        Frecuencias.DIARIA.label,
        Frecuencias.SEMANAL.label
    )
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
            }
        )
        if (checked) {
            Spacer(modifier = Modifier.padding(15.dp))
            Text(text = "Umbral de gasto o limite maximo", fontWeight = FontWeight.Bold)
            CurrencyTextField(
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .focusRequester(focusRequester)
                    .onFocusChanged { isFocused = it.isFocused },
                onChange = {
                    umbralValue = it
                },
                currencySymbol = "$",
                errorColor = Color.Red,
                locale = Locale.getDefault(),
                initialText = "0",
                maxNoOfDecimal = 0,
                errorText = "El formato no es correcto",
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.padding(30.dp))
            Text(text = "Frecuencia", fontWeight = FontWeight.Bold)
            val options = frecuencias.map { it ->
                val checkedFrecuencia = remember { mutableStateOf(false) }
                OptionCheckItem(
                    label = it,
                    checked = checkedFrecuencia.value,
                    onCheckedChange = { checkedFrecuencia.value = it }
                )
            }
            CheckboxListRow(options = options)
            Spacer(modifier = Modifier.size(30.dp))
            // Botón "Guardar Configuración"
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
}


@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    NotificationScreen()
}