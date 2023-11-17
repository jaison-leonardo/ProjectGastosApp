package com.iue.projectgastosapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.iue.projectgastosapp.navigation.AppNavigation
import com.iue.projectgastosapp.ui.theme.ProjectGastosAppTheme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_user")

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectGastosAppTheme {
                AppNavigation()
            }
        }
    }

}

