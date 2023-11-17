package com.iue.projectgastosapp.views.startscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iue.projectgastosapp.R
import com.iue.projectgastosapp.navigation.Routes
import com.iue.projectgastosapp.room.PreferenceDatastore
import com.iue.projectgastosapp.room.PreferencesData
import com.iue.projectgastosapp.ui.theme.BackgroundColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
//    val context = LocalContext.current
//    val preferencesData = PreferenceDatastore(context)
    LaunchedEffect(key1 = true) {
//        val preferences = PreferencesData()
//        preferencesData.saveData(preferences)
        delay(1000)
        navController.popBackStack()
        navController.navigate(Routes.LoginScreen.route)
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = BackgroundColor)),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_se),
            contentDescription = "Logo Splash Screen)",
            modifier = Modifier
                .size(200.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(navController = NavController(LocalContext.current))
}