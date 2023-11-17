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

fun getIdByUserAndDate(userId: String, dateToCheck: Date, callback: (Int?, String) -> Unit) {
    val metasReference = Firebase.database.reference
        .child(Objects.USUARIOS.label)
        .child(userId)
        .child(Objects.METAS.label)

    metasReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                var metaId: Int? = null
                var isdate = false
                snapshot.children.forEach { metaSnapshot ->
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
                    }
                    metaId = maxOf(metaId ?: 0, metaSnapshot.key?.toIntOrNull() ?: 0)
                }
                callback(if (isdate) metaId else metaId?.plus(1), "metaId")
            } else {
                callback(1, "No hay datos de metas para este usuario")
            }

        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "")
        }
    })
}

