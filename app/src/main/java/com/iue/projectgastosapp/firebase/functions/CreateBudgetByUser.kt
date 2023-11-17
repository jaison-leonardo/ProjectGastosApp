package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Objects
import com.iue.projectgastosapp.firebase.dataobjects.DataPresupuesto

fun createBudgetByUser(
    userBudget: String,
    idBudget: Int,
    budget: DataPresupuesto,
    callback: (Boolean, String) -> Unit
) {
    val reference =
        Firebase.database.reference
            .child(Objects.USUARIOS.label)
            .child(userBudget)
            .child(Objects.PRESUPUESTOS.label)
    reference
        .child((idBudget + 1).toString())
        .setValue(budget)
        .addOnCompleteListener { createBudget ->
            if (createBudget.isSuccessful) {
                callback(true, "Presupuesto creado correctamente")
            } else {
                callback(false, "Error al crear el presupuesto")
            }
        }
}