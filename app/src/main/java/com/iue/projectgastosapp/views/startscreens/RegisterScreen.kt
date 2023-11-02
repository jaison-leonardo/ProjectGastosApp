package com.iue.projectgastosapp.views.startscreens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.R
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.checkIfEmailExists
import com.iue.projectgastosapp.navigation.Routes
import com.iue.projectgastosapp.views.composable.ShowDialog
import com.iue.projectgastosapp.views.composable.TopBar
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun RegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopBar(
            title = "Registro",
            icon = Icons.Default.ArrowBack,
            contentDescription = "Registro",
            onClickDrawer = { navController.popBackStack() }
        )
        BottomContentRegister(navController)
    }
}

@Composable
fun BottomContentRegister(navController: NavController) {
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var reEmail by remember { mutableStateOf("") }
    var isNameValid by remember { mutableStateOf(true) }
    var isLastNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isReEmailValid by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            // The user cancelled the login, was it due to an Exception?
            if (result.data?.action == ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST) {
                Log.e(
                    "LOG",
                    "Couldn't start One Tap UI: ${result.data?.extras?.getString("exception")} "
                )
            }
            return@rememberLauncherForActivityResult
        }
        val oneTapClient = Identity.getSignInClient(context)
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        if (idToken != null) {
            firebaseAuthWithGoogle(idToken) { isSuccessfull, dataUser ->
                if (isSuccessfull) {
                    if (dataUser != null) {
                        checkIfEmailExists(dataUser.email) { exists, messageEmail ->
                            if (exists) {
                                message = messageEmail
                                showDialog = true
                            } else {
                                val routeRegisterPinScreen =
                                    "${Routes.CreatePinScreen.route}//${dataUser.name}/${dataUser.lastName}/${dataUser.email}"
                                navController.navigate(routeRegisterPinScreen)
                            }
                        }
                    } else {
                        showDialog = true
                        message = "No se pudo registrar con Google, ocurrio un error con el email"
                    }
                } else {
                    showDialog = true
                    message = "No se pudo registrar con Google, ocurrio un error"
                }
            }
            Log.d("LOG", idToken)
        } else {
            showDialog = true
            message = "No se pudo obtener el token de la cuenta"
            Log.d("LOG", "Null Token")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Text(
            text = "Datos Personales",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        OutlinedTextField(
            value = nombres,
            onValueChange = {
                nombres = it
                isNameValid = it.isNotEmpty()
            },
            label = { Text("Nombres") },
            isError = !isNameValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        OutlinedTextField(
            value = apellidos,
            onValueChange = {
                apellidos = it
                isLastNameValid = it.isNotEmpty()
            },
            label = { Text("Apellidos") },
            isError = !isLastNameValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            isError = !isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        OutlinedTextField(
            value = reEmail,
            onValueChange = {
                reEmail = it
                isReEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches() && it == email
            },
            label = { Text("Repita el correo electrónico") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            isError = !isReEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        Button(
            onClick = {
                if (nombres.isNotEmpty() && apellidos.isNotEmpty() && email.isNotEmpty() &&
                    reEmail.isNotEmpty() && isNameValid && isLastNameValid && isEmailValid &&
                    isReEmailValid
                ) {
                    checkIfEmailExists(email.trim()) { exists, messageEmail ->
                        if (exists) {
                            message = messageEmail
                            showDialog = true
                        } else {
                            val routeRegisterPinScreen =
                                "${Routes.CreatePinScreen.route}/00000/${nombres.trim()}/${apellidos.trim()}/${email.trim()}"
                            navController.navigate(routeRegisterPinScreen)
                        }
                    }
                } else {
                    message = "Por favor corrija los datos suministrados"
                    showDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
        ) {
            Row {
                Text(text = "Continuar", color = Color.White)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Text(
            text = "OR", modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                scope.launch {
                    signIn(context, launcher)
                }

            },
            modifier = Modifier
                .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                .fillMaxWidth()
                .shadow(1.dp, CircleShape, true),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                )
                Text(
                    text = "Registrar con Google",
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
}

suspend fun signIn(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(
                    context.getString(R.string.default_web_client_id)
                )
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    try {
        // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
        // Instead of listeners that aren't cleaned up automatically
        val result = oneTapClient.beginSignIn(signInRequest).await()

        // Now construct the IntentSenderRequest the launcher requires
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        // No saved credentials found. Launch the One Tap sign-up flow, or
        // do nothing and continue presenting the signed-out UI.
        Log.d("LOG", e.message.toString())
    }
}

private fun firebaseAuthWithGoogle(idToken: String, callback: (Boolean, DataUser?) -> Unit) {
    val auth = Firebase.auth
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success
                task.result.user.let { user ->
                    Log.d("Auth_User", "User: ${user?.displayName} Email: ${user?.email}")
                    val names = user?.displayName?.split(" ")
                    var firstName = ""
                    var lastName = ""
                    names?.apply {
                        if (size > 1) {
                            firstName = get(0)
                            lastName = get(size - 1)
                        } else if (size == 1) {
                            firstName = get(0)
                        }
                    }

                    val dataUser = DataUser(
                        id = "",
                        name = firstName,
                        lastName = lastName,
                        email = user?.email!!
                    )
                    callback(true, dataUser)
                }
                // Access user information like user.displayName, user.email, etc.
            } else {
                // If sign in fails, display a message to the user.
                callback(false, null)
            }
        }
}
