package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.firebase.dataobjects.DataUser

// Crear cuenta en firebase
fun createAccount(dataUser: DataUser, pinValue: String, callback: (Boolean, String) -> Unit) {
    Firebase.auth.createUserWithEmailAndPassword(dataUser.email, pinValue)
        .addOnCompleteListener { createAuth ->
            if (createAuth.isSuccessful) {
                val userUid = createAuth.result?.user?.uid
                if (userUid != null) {
                    val bd = Firebase.database.reference
                    bd.child("users").child(userUid).setValue(dataUser)
                        .addOnCompleteListener { createDatauser ->
                            if (createDatauser.isSuccessful) {
                                callback(true, userUid)
                            } else {
                                callback(false, "Error al crear la cuenta Task Failed")
                            }
                        }
                } else {
                    callback(false, "Error al crear la cuenta userUid Null")
                }
            } else {
                callback(false, "Error al crear la cuenta Auth Failed")
            }
        }
}