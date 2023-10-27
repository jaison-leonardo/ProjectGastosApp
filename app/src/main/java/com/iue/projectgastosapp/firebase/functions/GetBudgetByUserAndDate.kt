package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.firebase.dataobjects.DataPresupuesto
import com.iue.projectgastosapp.utils.getDateObjFromString
import com.iue.projectgastosapp.utils.getFirstDayOfMonth
import com.iue.projectgastosapp.utils.getFormattedDate
import com.iue.projectgastosapp.utils.getLastDayOfMonth
import com.iue.projectgastosapp.utils.isDateInRange
import java.util.Date

fun getBudgetByUserAndDate(
    userId: String,
    dateToCheck: Date,
    callback: (DataPresupuesto?, String) -> Unit
) {
    val budgetReference = Firebase.database.reference
        .child("users")
        .child(userId)
        .child("presupuestos")

    budgetReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var budgetCurrent: DataPresupuesto? = null
            if (snapshot.exists()) {
                budgetCurrent = findBudgetForDate(snapshot, dateToCheck)
                if (budgetCurrent != null) {
                    callback(budgetCurrent, "budgetId")
                } else {
                    budgetCurrent = DataPresupuesto(
                        alimentacion = 0.0,
                        transporte = 0.0,
                        entretenimiento = 0.0,
                        otros = 0.0,
                        fechaPresupuesto = getFormattedDate(Date(), "yyyy-MM-dd")
                    )
                    callback(
                        budgetCurrent,
                        "No hay datos de presupuestos para este usuario en este periodo"
                    )
                }
            } else {
                budgetCurrent = DataPresupuesto(
                    alimentacion = 0.0,
                    transporte = 0.0,
                    entretenimiento = 0.0,
                    otros = 0.0,
                    fechaPresupuesto = getFormattedDate(Date(), "yyyy-MM-dd")
                )
                callback(budgetCurrent, "No hay datos de presupuestos para este usuario")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "")
        }
    })
}

private fun findBudgetForDate(snapshot: DataSnapshot, dateToCheck: Date): DataPresupuesto? {
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
            return DataPresupuesto(
                alimentacion = budgetSnapshot.child("alimentacion").value.toString()
                    .toDouble(),
                transporte = budgetSnapshot.child("transporte").value.toString()
                    .toDouble(),
                entretenimiento = budgetSnapshot.child("entretenimiento").value.toString()
                    .toDouble(),
                otros = budgetSnapshot.child("otros").value.toString()
                    .toDouble(),
                fechaPresupuesto = budgetSnapshot.child("fechaPresupuesto").value.toString()
            )
        }
    }

    return null
}
