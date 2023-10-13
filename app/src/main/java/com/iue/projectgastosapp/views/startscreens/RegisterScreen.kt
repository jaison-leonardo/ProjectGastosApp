package com.iue.projectgastosapp.views.startscreens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iue.projectgastosapp.R
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
            isError = !isNameValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        OutlinedTextField(
            value = apellidos,
            onValueChange = {
                apellidos = it
                isLastNameValid = it.isNotEmpty()
            },
            label = { Text("Apellidos") },
            isError = !isLastNameValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
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
                .padding(top = 10.dp)
        )
        OutlinedTextField(
            value = reEmail,
            onValueChange = {
                reEmail = it
                isReEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches() && it == email
            },
            label = { Text("Repita el correo electrónico") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            isError = !isReEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        Button(
            onClick = {
                if (nombres.isNotEmpty() && apellidos.isNotEmpty() && email.isNotEmpty() &&
                    reEmail.isNotEmpty() && isNameValid && isLastNameValid && isEmailValid &&
                    isReEmailValid
                ) {
                    val routeRegisterPinScreen =
                        "${Routes.CreatePinScreen.route}/$nombres/$apellidos/$email/false"
                    navController.navigate(routeRegisterPinScreen)
                } else {
                    message = "Por favor corrija los datos sumministrados"
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
        Text(
            text = "OR", modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Button(
            onClick = {

            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                .fillMaxWidth()
                .shadow(1.dp, CircleShape, true),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                )
                Text(
                    text = "Registrar con Google",
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Bold
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

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navController = NavController(LocalContext.current))
}
