package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun validatePinByEmail(email: String, pinValue: String, callback: (Boolean, String) -> Unit) {
    Firebase.auth.signInWithEmailAndPassword(email, pinValue)
        .addOnCompleteListener { signIn ->
            if (signIn.isSuccessful) {
                callback(true, "Ingreso correcto")
            } else {
                callback(false, "PIN incorrecto")
            }
        }
}