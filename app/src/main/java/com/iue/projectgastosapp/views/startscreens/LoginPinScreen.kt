package com.iue.projectgastosapp.views.startscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
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
            dataUser = dataUser,
            navController = navController
        )
    }
}
