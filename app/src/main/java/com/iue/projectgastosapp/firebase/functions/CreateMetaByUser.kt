package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.firebase.dataobjects.DataMetaAhorro
import com.iue.projectgastosapp.utils.getFirstDayOfMonth
import com.iue.projectgastosapp.utils.getFormattedDate
import com.iue.projectgastosapp.utils.sumarDiasAFecha
import java.util.Date

fun createMetaByUser(
    userMeta: String,
    idMeta: Int,
    meta: DataMetaAhorro,
    categoria: String,
    callback: (Boolean, String) -> Unit
) {
    val bd = Firebase.database.reference
    bd.child("users").child(userMeta).child("metas")
        .child((idMeta).toString())
        .updateChildren(mapOf(categoria to meta))
        .addOnCompleteListener { createMeta ->
            if (createMeta.isSuccessful) {
                bd.child("users").child(userMeta).child("metas")
                    .child((idMeta).toString()).child("fechaMes")
                    .setValue(
                        getFormattedDate(
                            sumarDiasAFecha(getFirstDayOfMonth(Date()), 5),
                            "yyyy-MM-dd"
                        )
                    )
                callback(true, "Meta creada correctamente")
            } else {
                callback(false, "Error al crear la meta")
            }
        }
}