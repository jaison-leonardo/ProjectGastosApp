package com.iue.projectgastosapp.views.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.views.startscreens.DataUser

@Composable
fun ComponentPin(
    title: String,
    subtitle: String,
    paddingTop: Int,
    validatePin: Boolean,
    dataUser: DataUser? = null
) {
    var textSubtitle by remember { mutableStateOf(subtitle) }
    var textButton by remember { mutableStateOf("Aceptar") }
    var pinValue by remember { mutableStateOf("") }
    var rePinValue by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = paddingTop.dp)
        )
        Text(
            text = textSubtitle,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        PasswordTextField(
            value = pinValue,
            onValueChange = { newValue -> pinValue = newValue },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("PIN") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        )
        Button(
            onClick = {
                if (validatePin) {
                    if (pinValue.isNotEmpty()) {
                        textSubtitle = "Por favor repita su PIN"
                        textButton = "Confirmar"
                        if (rePinValue == "") {
                            rePinValue = pinValue
                        } else {
                            if (rePinValue == pinValue) {
                                textSubtitle = "PIN creado correctamente"
                                textButton = "Aceptar"
                                // Navegar hasta la pantalla de login
                            } else {
                                // Mostrar mensaje de error por no coincidir y regresar
                                textSubtitle = "Por favor ingrese su nuevo PIN"
                                textButton = "Aceptar"
                                pinValue = ""
                                rePinValue = ""
                            }
                        }
                    } else {
                        // Mostrar mensaje de error por vacio
                    }
                } else {
                    if (pinValue.isNotEmpty() && validatePinByEmail(dataUser, pinValue)) {
                        // Navegar hasta la pantalla de inicio
                    } else {
                        // Mostrar mensaje de error por vacio o no coincidir
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
        ) {
            Row {
                Text(text = textButton, color = Color.White)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

fun validatePinByEmail(dataUser: DataUser?, pinValue: String): Boolean {
    // Validar en firebase si el pin es correcto
    return true
}
