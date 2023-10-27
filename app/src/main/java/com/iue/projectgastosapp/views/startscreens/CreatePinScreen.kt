package com.iue.projectgastosapp.views.startscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.views.composable.ComponentPin
import com.iue.projectgastosapp.views.composable.TopBar

@Composable
fun CreatePinScreen(
    navController: NavController,
    dataUser: DataUser
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        TopBar(
            title = "Crear PIN",
            icon = Icons.Default.ArrowBack,
            contentDescription = "Crear PIN",
            onClickDrawer = { navController.popBackStack() }
        )
        ComponentPin(
            title = "Crear PIN",
            subtitle = "Por favor ingrese su nuevo PIN",
            paddingTop = 100,
            validatePin = true,
            navController = navController,
            dataUser = dataUser
        )
    }
}
