package com.iue.projectgastosapp.firebase.functions

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun checkIfEmailExists(email: String, callback: (Boolean, String) -> Unit) {
    Firebase.auth.fetchSignInMethodsForEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result?.signInMethods
                val check = task.result.signInMethods!!.isNotEmpty()
                if (!result.isNullOrEmpty()) {
                    callback(true, "El correo electrónico ya se encuentra registrado")
                } else {
                    callback(false, "El correo electrónico no se encuentra registrado")
                }
            } else {
                callback(false, "Error al verificar el email")
            }

        }
}