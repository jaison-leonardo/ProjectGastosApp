package com.iue.projectgastosapp.views.startscreens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iue.projectgastosapp.navigation.Routes
import com.iue.projectgastosapp.views.composable.TopContentStart

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopContentStart()
        BottomContentLogin(navController)
    }
}


@Composable
fun BottomContentLogin(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp),
    ) {
        Text(
            text = "BIENVENIDO",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Ingrese su correo electrónico",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            isError = !isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        )

        Button(
            onClick = {
                if (email.isNotEmpty() && isEmailValid && checkIfEmailExists(email)) {
                    val dataUser = getDataUser(email)
                    val routeLoginPinScreen =
                        "${Routes.LoginPinScreen.route}/" +
                                "${dataUser.name}/${dataUser.lastName}/${dataUser.email}/" +
                                "${dataUser.isAuth}"
                    navController.navigate(routeLoginPinScreen)
                } else {
                    showDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
        ) {
            Row {
                Text(text = "Continuar", color = Color.White)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "¿No tiene cuenta?")
            Text(text = "Haga clic aquí",
                color = Color(0xFF2196F3),
                modifier = Modifier
                    .clickable {
                        navController.navigate(Routes.RegisterScreen.route)
                    }
                    .padding(start = 4.dp))
        }
    }
    ShowDialog(
        show = showDialog,
        message = "Correo electrónico no válido",
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
}

private fun checkIfEmailExists(email: String): Boolean {
    // Ir a firebase y verificar si el correo existe
    return true
}

private fun getDataUser(email: String): DataUser {
    // Ir a firebase y obtener los datos del usuario
    return DataUser("Jaison", "Arboleda", email, false)
}

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


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = NavController(LocalContext.current))
}