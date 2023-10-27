package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.firebase.dataobjects.DataGasto

fun getGastosByUser(idUser: String, callback: (ArrayList<DataGasto>?, String) -> Unit) {
    val dbReference = Firebase.database.reference.child("users").child(idUser).child("gastos")
    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val gastosList = ArrayList<DataGasto>()
                for (gastoSnapshot in snapshot.children) {
                    val gasto = DataGasto(
                        cantidadGasto = gastoSnapshot.child("cantidadGasto").value.toString()
                            .toDouble(),
                        fechaGasto = gastoSnapshot.child("fechaGasto").value.toString(),
                        descripcionGasto = gastoSnapshot.child("descripcionGasto").value.toString(),
                        categoriaGasto = gastoSnapshot.child("categoriaGasto").value.toString()
                    )
                    gastosList.add(gasto)
                }
                callback(gastosList, "Gastos encontrados")
            } else {
                callback(null, "No se encontraron gastos")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "Error al obtener los gastos")
        }
    })
}