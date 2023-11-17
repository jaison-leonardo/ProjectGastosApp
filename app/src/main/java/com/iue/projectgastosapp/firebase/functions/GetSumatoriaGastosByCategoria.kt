package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.enums.Objects
import com.iue.projectgastosapp.firebase.dataobjects.GastosAndCategoria
import com.iue.projectgastosapp.utils.getDateObjFromString
import com.iue.projectgastosapp.utils.getFirstDayOfMonth
import com.iue.projectgastosapp.utils.getLastDayOfMonth
import com.iue.projectgastosapp.utils.isDateInRange
import com.iue.projectgastosapp.utils.truncateTimeFromDate
import java.util.Date


fun getSumatoriaGastosByDateAndCategoria(
    idUser: String,
    categorias: List<Categories>,
    callback: (ArrayList<GastosAndCategoria>?, String) -> Unit
) {
    val dbReference = Firebase.database.reference
        .child(Objects.USUARIOS.label)
        .child(idUser)
        .child(Objects.GASTOS.label)

    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val fechaMesMin = truncateTimeFromDate(getFirstDayOfMonth(Date()))
                val fechaMesMax = truncateTimeFromDate(getLastDayOfMonth(Date()))
                val sumCategoriasList = categorias.map { categoria ->
                    val sumatoriaGastos = snapshot.children
                        .filter {
                            val gastoDate = getDateObjFromString(
                                it.child("fechaGasto").value.toString(),
                                "yyyy-MM-dd"
                            )
                            it.child("categoriaGasto").value.toString() == categoria.id &&
                                    isDateInRange(gastoDate, fechaMesMin, fechaMesMax)
                        }
                        .sumOf { it.child("cantidadGasto").value.toString().toDouble() }
                    GastosAndCategoria(categoria.id, sumatoriaGastos)
                }.toCollection(ArrayList())
                callback(sumCategoriasList, "Sumatoria de gastos encontrada")
            } else {
                val sumCategoriasList = categorias.map { categoria ->
                    GastosAndCategoria(categoria.id, 0.0)
                }.toCollection(ArrayList())
                callback(sumCategoriasList, "No se encontraron gastos")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "Error al obtener los gastos")
        }
    })
}
