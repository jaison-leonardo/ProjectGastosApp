package com.iue.projectgastosapp.views.startscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.iue.projectgastosapp.views.composable.ComponentPin
import com.iue.projectgastosapp.views.composable.TopContentStart

@Composable
fun LoginPinScreen(navController: NavController, dataUser: DataUser) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        TopContentStart()
        ComponentPin(
            title = "BIENVENIDO ${dataUser.name.uppercase()} ${dataUser.lastName.uppercase()}",
            subtitle = "Antes de continuar ingrese su PIN",
            paddingTop = 10,
            validatePin = false,
            dataUser = dataUser
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPinScreenPreview() {
    val navController = rememberNavController()
    LoginPinScreen(
        navController = navController,
        dataUser = DataUser("Jhon", "Doe", "jaison@jaison.com", false)
    )
}