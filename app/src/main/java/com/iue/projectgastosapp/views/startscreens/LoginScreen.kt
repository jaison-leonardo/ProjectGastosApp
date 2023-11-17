package com.iue.projectgastosapp.views.startscreens

import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iue.projectgastosapp.firebase.functions.checkIfEmailExists
import com.iue.projectgastosapp.firebase.functions.getDataUserByEmail
import com.iue.projectgastosapp.navigation.Routes
import com.iue.projectgastosapp.views.composable.ShowCircularIndicator
import com.iue.projectgastosapp.views.composable.ShowDialog
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
    var showCircularIndicator by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


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
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            isError = !isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusManager.clearFocus()
                        return@onKeyEvent true
                    }
                    return@onKeyEvent false
                }
        )

        Button(
            onClick = {
                if (email.isNotEmpty() && isEmailValid) {
                    showCircularIndicator = true
                    checkIfEmailExists(email) { exists, messageEmail ->
                        if (exists) {
                            getDataUserByEmail(email) { dataUser, messageUser ->
                                showCircularIndicator = false
                                if (dataUser != null) {
                                    val routeLoginPinScreen =
                                        "${Routes.LoginPinScreen.route}/" +
                                                "${dataUser.id}/${dataUser.name}/${dataUser.lastName}/${dataUser.email}"
                                    Log.i("routeLoginPinScreen", routeLoginPinScreen)
                                    navController.navigate(routeLoginPinScreen)
                                } else {
                                    showDialog = true
                                    message = messageUser
                                }
                            }
                        } else {
                            showCircularIndicator = false
                            showDialog = true
                            message = messageEmail
                            focusManager.moveFocus(FocusDirection.Up)
                        }
                    }
                } else {
                    message = "El correo electrónico no es válido"
                    showDialog = true
                    focusManager.moveFocus(FocusDirection.Up)
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
    ShowCircularIndicator(show = showCircularIndicator)
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
}
