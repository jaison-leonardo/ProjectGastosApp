package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.utils.getDateObjFromString
import com.iue.projectgastosapp.utils.getFirstDayOfMonth
import com.iue.projectgastosapp.utils.getLastDayOfMonth
import com.iue.projectgastosapp.utils.isDateInRange
import java.util.Date

fun getIdByUserAndDate(userId: String, dateToCheck: Date, callback: (Int?, String) -> Unit) {
    val metasReference = Firebase.database.reference
        .child("users")
        .child(userId)
        .child("metas")

    metasReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                var metaId: Int? = null
                var isdate = false
                for (metaSnapshot in snapshot.children) {
                    val fechaPresupuesto = metaSnapshot.child("fechaMes").value.toString()
                    val fechaPresupuestoMin =
                        getFirstDayOfMonth(
                            getDateObjFromString(fechaPresupuesto, "yyyy-MM-dd")
                        )
                    val fechaPresupuestoMax = getLastDayOfMonth(
                        getDateObjFromString(fechaPresupuesto, "yyyy-MM-dd")
                    )

                    if (isDateInRange(dateToCheck, fechaPresupuestoMin, fechaPresupuestoMax)) {
                        isdate = true
                        metaId = metaSnapshot.key?.toIntOrNull()
                        break
                    }
                    val expenseNumber = metaSnapshot.key?.toIntOrNull()

                    if ((expenseNumber != null) && ((metaId == null) || (expenseNumber > metaId))) {
                        metaId = expenseNumber
                    }
                }
                if (metaId != null) {
                    callback(if (isdate) metaId else metaId + 1, "metaId")
                } else {
                    callback(null, "")
                }
            } else {
                callback(1, "No hay datos de metas para este usuario")
            }

        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "")
        }
    })
}