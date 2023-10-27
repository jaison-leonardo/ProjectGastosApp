package com.iue.projectgastosapp.views.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.NavController
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.createAccount
import com.iue.projectgastosapp.firebase.functions.validatePinByEmail
import com.iue.projectgastosapp.navigation.Routes

@Composable
fun ComponentPin(
    title: String,
    subtitle: String,
    paddingTop: Int,
    validatePin: Boolean,
    dataUser: DataUser? = null,
    navController: NavController
) {
    var textSubtitle by remember { mutableStateOf(subtitle) }
    var textButton by remember { mutableStateOf("Aceptar") }
    var pinValue by remember { mutableStateOf("") }
    var rePinValue by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
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
                    if (pinValue.length >= 6) {
                        textSubtitle = "Por favor repita su PIN"
                        textButton = "Confirmar"
                        if (rePinValue == "") {
                            rePinValue = pinValue
                            pinValue = ""
                        } else {
                            if (rePinValue == pinValue) {
                                textSubtitle = "PIN creado correctamente"
                                textButton = "Aceptar"
                                message = "PIN creado correctamente"
                                // Navegar hasta la pantalla de login
                                isLoading = true
                                createAccount(dataUser!!, pinValue) { isSuccess, response ->
                                    isLoading = false
                                    if (isSuccess) {
                                        // Navegar hasta la pantalla de inicio
                                        navController.navigate(Routes.LoginScreen.route)
                                    } else {
                                        // Mostrar mensaje de error
                                        message = response
                                        showDialog = true
                                    }
                                }
                            } else {
                                // Mostrar mensaje de error por no coincidir y regresar
                                message = "PIN no coincide"
                                showDialog = true
                                textSubtitle = "Por favor ingrese su nuevo PIN"
                                textButton = "Aceptar"
                                pinValue = ""
                                rePinValue = ""
                            }
                        }
                    } else {
                        // Mostrar mensaje de error por vacio
                        message = "El PIN debe tener minimo 6 dígitos"
                        showDialog = true
                    }
                } else {
                    if (pinValue.isNotEmpty()) {
                        if (dataUser != null) {
                            validatePinByEmail(dataUser.email, pinValue) { isSuccess, response ->
                                if (isSuccess) {
                                    // Navegar hasta la pantalla de inicio
                                    val routeMenuDrawerScreen =
                                        "${Routes.MenuDrawerScreen.route}/${dataUser.id}/${dataUser.name}/" +
                                                "${dataUser.lastName}/${dataUser.email}"
                                    navController.navigate(routeMenuDrawerScreen)
                                } else {
                                    // Mostrar mensaje de error por vacio o no coincidir
                                    message = response
                                    showDialog = true
                                }
                            }
                        }else {
                            message = "Error al obtener los datos del usuario"
                            showDialog = true
                        }
                    } else {
                        // Mostrar mensaje de error por vacio o no coincidir
                        message = "No puede ser vacio"
                        showDialog = true
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
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .padding(16.dp)
        )
    }
}



