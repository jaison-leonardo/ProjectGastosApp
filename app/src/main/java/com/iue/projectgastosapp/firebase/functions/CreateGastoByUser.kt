package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.firebase.dataobjects.DataGasto

fun createGastoByUser(
    userGasto: String,
    idGasto: Int,
    gasto: DataGasto,
    callback: (Boolean, String) -> Unit
) {
    val bd = Firebase.database.reference
    bd.child("users").child(userGasto).child("gastos")
        .child((idGasto + 1).toString())
        .setValue(gasto)
        .addOnCompleteListener { createGasto ->
            if (createGasto.isSuccessful) {
                callback(true, "Gasto creado correctamente")
            } else {
                callback(false, "Error al crear el gasto")
            }
        }
}