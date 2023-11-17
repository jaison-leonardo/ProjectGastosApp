package com.iue.projectgastosapp.navigation

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splashScreen")
    object LoginScreen : Routes("loginScreen")
    object LoginPinScreen : Routes("loginPinScreen")
    object RegisterScreen : Routes("registerScreen")
    object CreatePinScreen : Routes("crearPinScreen")
    object MenuDrawerScreen : Routes("menuDrawerScreen")
}
