package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.firebase.dataobjects.GastosAndCategoria


fun getSumatoriaGastosByCategoria(
    idUser: String,
    categorias: List<Categories>,
    callback: (ArrayList<GastosAndCategoria>?, String) -> Unit
) {
    val dbReference = Firebase.database.reference.child("users").child(idUser)
        .child("gastos")

    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val sumCategoriasList = ArrayList<GastosAndCategoria>()
            if (snapshot.exists()) {
                for (categoria in categorias) {
                    val sumatoriaGastos = snapshot.children
                        .filter { it.child("categoriaGasto").value.toString() == categoria.id }
                        .sumOf { it.child("cantidadGasto").value.toString().toDouble() }
                    sumCategoriasList.add(GastosAndCategoria(categoria.id, sumatoriaGastos))
                }
                callback(sumCategoriasList, "Sumatoria de gastos encontrada")
            } else {
                callback(null, "No se encontraron gastos")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "Error al obtener los gastos")
        }
    })
}