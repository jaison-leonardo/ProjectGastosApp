package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Objects
import com.iue.projectgastosapp.utils.getDateObjFromString
import com.iue.projectgastosapp.utils.getFirstDayOfMonth
import com.iue.projectgastosapp.utils.getLastDayOfMonth
import com.iue.projectgastosapp.utils.isDateInRange
import java.util.Date

fun checkIfDateExists(userId: String, dateToCheck: Date, callback: (Boolean, String) -> Unit) {
    val budgetReference = Firebase.database.reference
        .child(Objects.USUARIOS.label)
        .child(userId)
        .child(Objects.PRESUPUESTOS.label)

    budgetReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var dateExists = false
            var message = "Fecha no encontrada"

            for (budgetSnapshot in snapshot.children) {
                val fechaPresupuesto = budgetSnapshot.child("fechaPresupuesto").value.toString()
                val fechaPresupuestoMin =
                    getFirstDayOfMonth(
                        getDateObjFromString(fechaPresupuesto, "yyyy-MM-dd")
                    )
                val fechaPresupuestoMax = getLastDayOfMonth(
                    getDateObjFromString(fechaPresupuesto, "yyyy-MM-dd")
                )

                if (isDateInRange(dateToCheck, fechaPresupuestoMin, fechaPresupuestoMax)) {
                    dateExists = true
                    message = "Ya existe un presupuesto asignado en este mes"
                    break
                }
            }

            callback(dateExists, message)
        }

        override fun onCancelled(error: DatabaseError) {
            callback(false, "")
        }
    })
}