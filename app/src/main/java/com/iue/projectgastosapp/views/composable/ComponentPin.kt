package com.iue.projectgastosapp.views.composable

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.iue.projectgastosapp.MainActivity
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.createAccount
import com.iue.projectgastosapp.firebase.functions.validatePinByEmail
import com.iue.projectgastosapp.navigation.Routes
import com.iue.projectgastosapp.room.PreferenceDatastore
import com.iue.projectgastosapp.room.PreferencesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ComponentPin(
    title: String,
    subtitle: String,
    paddingTop: Int,
    validatePin: Boolean,
    dataUser: DataUser? = null,
    navController: NavController
) {
    var textSubtitle by remember { mutableStateOf(subtitle) }
    var textButton by remember { mutableStateOf("Aceptar") }
    var pinValue by remember { mutableStateOf("") }
    var rePinValue by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var authBiometric by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val preferencesData = PreferenceDatastore(context)
    lateinit var prompInfo: PromptInfo
    LaunchedEffect(key1 = true) {
        preferencesData.getData().collect {
            withContext(Dispatchers.Main) {
                Log.d("SplashScreen_data", it.toString())
                if (it.authAlternativa) {
                    isLoading = true
                    val activity = context as? MainActivity
                    setupAuth(activity!!) { authentication, prompInfoSetup ->
                        prompInfo = prompInfoSetup
                        authenticate(activity, authentication, prompInfo) { authValidate ->
                            authBiometric = authValidate
                            if (dataUser != null) {
                                Log.i("Autenticacion_biometrica", "Exitosa")
                                val routeMenuDrawerScreen =
                                    "${Routes.MenuDrawerScreen.route}/${dataUser.id}/${dataUser.name}/" +
                                            "${dataUser.lastName}/${dataUser.email}"
                                navController.navigate(routeMenuDrawerScreen)
                            } else {
                                message = "Error al obtener los datos del usuario"
                                showDialog = true
                            }
                            isLoading = false
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = paddingTop.dp)
        )
        Text(
            text = textSubtitle,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        PasswordTextField(
            value = pinValue,
            onValueChange = { newValue -> pinValue = newValue },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("PIN") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        )
        Button(
            onClick = {
                isLoading = true
                if (validatePin) {
                    if (pinValue.length >= 6) {
                        textSubtitle = "Por favor repita su PIN"
                        textButton = "Confirmar"
                        if (rePinValue == "") {
                            rePinValue = pinValue
                            pinValue = ""
                        } else {
                            if (rePinValue == pinValue) {
                                textSubtitle = "PIN creado correctamente"
                                textButton = "Aceptar"
                                message = "PIN creado correctamente"
                                // Navegar hasta la pantalla de login
                                createAccount(dataUser!!, pinValue) { isSuccess, response ->
                                    if (isSuccess) {
                                        // Navegar hasta la pantalla de inicio
                                        lifecycleOwner.lifecycleScope.launch {
                                            val preferences = PreferencesData()
                                            preferencesData.saveData(preferences)
                                        }
                                        navController.navigate(Routes.LoginScreen.route)
                                    } else {
                                        // Mostrar mensaje de error
                                        message = response
                                        showDialog = true
                                    }
                                }
                            } else {
                                // Mostrar mensaje de error por no coincidir y regresar
                                message = "PIN no coincide"
                                showDialog = true
                                textSubtitle = "Por favor ingrese su nuevo PIN"
                                textButton = "Aceptar"
                                pinValue = ""
                                rePinValue = ""
                            }
                        }
                    } else {
                        // Mostrar mensaje de error por vacio
                        message = "El PIN debe tener minimo 6 dígitos"
                        showDialog = true
                    }
                    isLoading = false
                } else {
                    if (pinValue.isNotEmpty()) {
                        if (dataUser != null) {
                            if (!authBiometric) {
                                validatePinByEmail(
                                    dataUser.email,
                                    pinValue
                                ) { isSuccess, response ->
                                    isLoading = false
                                    if (isSuccess) {
                                        val routeMenuDrawerScreen =
                                            "${Routes.MenuDrawerScreen.route}/${dataUser.id}/${dataUser.name}/" +
                                                    "${dataUser.lastName}/${dataUser.email}"
                                        navController.navigate(routeMenuDrawerScreen)
                                    } else {
                                        // Mostrar mensaje de error por vacio o no coincidir
                                        message = response
                                        showDialog = true
                                    }
                                }
                            }
                        } else {
                            message = "Error al obtener los datos del usuario"
                            showDialog = true
                        }
                    } else {
                        // Mostrar mensaje de error por vacio o no coincidir
                        message = "No puede ser vacio"
                        showDialog = true
                    }
                }
                isLoading = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
        ) {
            Row {
                Text(text = textButton, color = Color.White)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .padding(16.dp)
        )
    }
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )

}

private fun setupAuth(
    context: MainActivity,
    canAuthenticate: (authentication: Boolean, prompInfo: PromptInfo) -> Unit
) {
    if (BiometricManager.from(context)
            .canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            ) == BiometricManager.BIOMETRIC_SUCCESS
    ) {

        val prompInfo: PromptInfo = PromptInfo.Builder()
            .setTitle("Autenticación Biometrica")
            .setSubtitle("Por favor autentiquese para continuar")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
            .build()
        canAuthenticate(true, prompInfo)
    }
}

private fun authenticate(
    context: MainActivity,
    canAuthenticate: Boolean,
    prompInfo: PromptInfo,
    auth: (auth: Boolean) -> Unit
) {
    if (canAuthenticate) {
        BiometricPrompt(context, ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    auth(true)
                }
            }).authenticate(prompInfo)
    }
}

