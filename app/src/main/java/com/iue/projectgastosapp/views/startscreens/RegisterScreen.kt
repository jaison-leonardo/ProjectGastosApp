package com.iue.projectgastosapp.views.startscreens

import android.util.Patterns
import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iue.projectgastosapp.firebase.functions.checkIfEmailExists
import com.iue.projectgastosapp.navigation.Routes
import com.iue.projectgastosapp.views.composable.ShowDialog
import com.iue.projectgastosapp.views.composable.TopBar


@Composable
fun RegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopBar(
            title = "Registro",
            icon = Icons.Default.ArrowBack,
            contentDescription = "Registro",
            onClickDrawer = { navController.popBackStack() }
        )
        BottomContentRegister(navController)
    }
}

@Composable
fun BottomContentRegister(navController: NavController) {
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var reEmail by remember { mutableStateOf("") }
    var isNameValid by remember { mutableStateOf(true) }
    var isLastNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isReEmailValid by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Text(
            text = "Datos Personales",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        OutlinedTextField(
            value = nombres,
            onValueChange = {
                nombres = it
                isNameValid = it.isNotEmpty()
            },
            label = { Text("Nombres") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone =
                {
                    focusManager.clearFocus()
                }
            ),
            isError = !isNameValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusManager.clearFocus()
                        focusManager.moveFocus(FocusDirection.Down)
                        return@onKeyEvent true
                    }
                    return@onKeyEvent false
                }
        )
        OutlinedTextField(
            value = apellidos,
            onValueChange = {
                apellidos = it
                isLastNameValid = it.isNotEmpty()
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone =
                {
                    focusManager.clearFocus()
                }
            ),
            label = { Text("Apellidos") },
            isError = !isLastNameValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusManager.clearFocus()
                        focusManager.moveFocus(FocusDirection.Down)
                        return@onKeyEvent true
                    }
                    return@onKeyEvent false
                }
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone =
                {
                    focusManager.clearFocus()
                }
            ),
            isError = !isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusManager.clearFocus()
                        focusManager.moveFocus(FocusDirection.Down)
                        return@onKeyEvent true
                    }
                    return@onKeyEvent false
                }
        )
        OutlinedTextField(
            value = reEmail,
            onValueChange = {
                reEmail = it
                isReEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches() && it == email
            },
            label = { Text("Repita el correo electrónico") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone =
                {
                    focusManager.clearFocus()
                }
            ),
            isError = !isReEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
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
                if (nombres.isNotEmpty() && apellidos.isNotEmpty() && email.isNotEmpty() &&
                    reEmail.isNotEmpty() && isNameValid && isLastNameValid && isEmailValid &&
                    isReEmailValid
                ) {
                    checkIfEmailExists(email.trim()) { exists, messageEmail ->
                        if (exists) {
                            message = messageEmail
                            showDialog = true
                        } else {
                            val routeRegisterPinScreen =
                                "${Routes.CreatePinScreen.route}/null/${nombres.trim()}/${apellidos.trim()}/${email.trim()}"
                            navController.navigate(routeRegisterPinScreen)
                        }
                    }
                } else {
                    message = "Por favor corrija los datos suministrados"
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
    }
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
}
