package com.iue.projectgastosapp.views.startscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.iue.projectgastosapp.views.composable.ComponentPin
import com.iue.projectgastosapp.views.composable.TopBar

@Composable
fun CrearPinScreen(navController: NavController) {
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
            validatePin = true
        )
    }
}



@Preview(showBackground = true)
@Composable
fun CrearPinScreenPreview() {
    val navController = rememberNavController()
    CrearPinScreen(navController)
}