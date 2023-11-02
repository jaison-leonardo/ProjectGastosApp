package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Objects
import com.iue.projectgastosapp.firebase.dataobjects.DataGasto

fun getGastosByUser(idUser: String, callback: (ArrayList<DataGasto>?, String) -> Unit) {
    val dbReference = Firebase.database.reference
        .child(Objects.USUARIOS.label)
        .child(idUser)
        .child(Objects.GASTOS.label)

    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val gastosList = snapshot.children.mapNotNull { gastoSnapshot ->
                gastoSnapshot.getValue(DataGasto::class.java)
            }
            if (gastosList.isNotEmpty()) {
                callback(ArrayList(gastosList), "Gastos encontrados")
            } else {
                callback(null, "No se encontraron gastos")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "Error al obtener los gastos")
        }
    })
}
