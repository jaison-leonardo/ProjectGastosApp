package com.iue.projectgastosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.iue.projectgastosapp.navigation.AppNavigation
import com.iue.projectgastosapp.ui.theme.ProjectGastosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectGastosAppTheme {
                AppNavigation()
            }
        }
    }
}
