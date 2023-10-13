package com.iue.projectgastosapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iue.projectgastosapp.R
import com.iue.projectgastosapp.views.homescreens.MenuDrawerScreen
import com.iue.projectgastosapp.views.startscreens.CreatePinScreen
import com.iue.projectgastosapp.views.startscreens.DataUser
import com.iue.projectgastosapp.views.startscreens.LoginPinScreen
import com.iue.projectgastosapp.views.startscreens.LoginScreen
import com.iue.projectgastosapp.views.startscreens.RegisterScreen
import com.iue.projectgastosapp.views.startscreens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.SplashScreen.route) {
        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Routes.LoginScreen.route) {
            LoginScreen(navController)
        }

        composable(
            route = "${Routes.LoginPinScreen.route}/{name}/{lastName}/{email}/{isAuth}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("isAuth") { type = NavType.BoolType },
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val lastName = backStackEntry.arguments?.getString("lastName")
            val email = backStackEntry.arguments?.getString("email")
            val isAuth = backStackEntry.arguments?.getBoolean("isAuth")
            Log.i("route", "Holi")
            LoginPinScreen(navController, DataUser(name!!, lastName!!, email!!, isAuth!!))
        }
        composable(Routes.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(
            route = "${Routes.CreatePinScreen.route}/{name}/{lastName}/{email}/{isAuth}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("isAuth") { type = NavType.BoolType },
            )
        ) {backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val lastName = backStackEntry.arguments?.getString("lastName")
            val email = backStackEntry.arguments?.getString("email")
            val isAuth = backStackEntry.arguments?.getBoolean("isAuth")
            CreatePinScreen(navController, DataUser(name!!, lastName!!, email!!, isAuth!!))
        }
        composable(
            route = "${Routes.MenuDrawerScreen.route}/{name}/{lastName}/{email}/{isAuth}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("isAuth") { type = NavType.BoolType },
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val lastName = backStackEntry.arguments?.getString("lastName")
            val email = backStackEntry.arguments?.getString("email")
            val isAuth = backStackEntry.arguments?.getBoolean("isAuth")
            val profileImage = painterResource(id = R.drawable.logo)
            MenuDrawerScreen(profileImage, navController, DataUser(name!!, lastName!!, email!!, isAuth!!))
        }
    }
}