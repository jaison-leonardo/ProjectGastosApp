package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Objects

fun getIdObjectByUser(idUser: String, child: String, callback: (Int?, String) -> Unit) {
    val dbReference = Firebase.database.reference
        .child(Objects.USUARIOS.label)
        .child(idUser)
        .child(child)

    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val maxExpenseNumber = snapshot.children
                    .mapNotNull { it.key?.toIntOrNull() }
                    .maxOrNull()
                callback(maxExpenseNumber, "$child encontrado")
            } else {
                callback(0, "No hay datos de $child para este usuario")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "Ha ocurrido un error generando el ID para el gasto")
        }
    })
}