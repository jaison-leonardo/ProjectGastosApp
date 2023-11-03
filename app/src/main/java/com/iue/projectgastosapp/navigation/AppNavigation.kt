package com.iue.projectgastosapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iue.projectgastosapp.R
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.views.sescreens.MenuDrawerScreen
import com.iue.projectgastosapp.views.startscreens.CreatePinScreen
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
            route = "${Routes.LoginPinScreen.route}/{id}/{name}/{lastName}/{email}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val name = backStackEntry.arguments?.getString("name")
            val lastName = backStackEntry.arguments?.getString("lastName")
            val email = backStackEntry.arguments?.getString("email")
            LoginPinScreen(navController, DataUser(id!!, name!!, lastName!!, email!!))
        }
        composable(Routes.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(
            route = "${Routes.CreatePinScreen.route}/{id}/{name}/{lastName}/{email}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            var idUser = backStackEntry.arguments?.getString("id")
            idUser = if (idUser != "null") idUser else ""
            val name = backStackEntry.arguments?.getString("name")
            val lastName = backStackEntry.arguments?.getString("lastName")
            val email = backStackEntry.arguments?.getString("email")
            CreatePinScreen(
                navController,
                DataUser(idUser!!, name!!, lastName!!, email!!)
            )
        }
        composable(
            route = "${Routes.MenuDrawerScreen.route}/{id}/{name}/{lastName}/{email}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val name = backStackEntry.arguments?.getString("name")
            val lastName = backStackEntry.arguments?.getString("lastName")
            val email = backStackEntry.arguments?.getString("email")
            val profileImage = painterResource(id = R.drawable.logo)
            MenuDrawerScreen(
                profileImage,
                navController,
                DataUser(id!!, name!!, lastName!!, email!!)
            )
        }
    }
}