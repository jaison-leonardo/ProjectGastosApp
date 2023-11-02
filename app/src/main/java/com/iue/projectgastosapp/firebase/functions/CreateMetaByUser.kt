package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Objects
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
    val reference = Firebase.database.reference
        .child(Objects.USUARIOS.label)
        .child(userMeta)
        .child(Objects.METAS.label)
    val metaId = (idMeta).toString()
    reference
        .child(metaId)
        .updateChildren(mapOf(categoria to meta))
        .addOnCompleteListener { createMeta ->
            if (createMeta.isSuccessful) {
                setMetaFecha(userMeta, metaId)
                callback(true, "Meta creada correctamente")
            } else {
                callback(false, "Error al crear la meta")
            }
        }
}

private fun setMetaFecha(userMeta: String, metaId: String) {
    val fechaMesPath = "${Objects.USUARIOS.label}/$userMeta/${Objects.METAS.label}/$metaId/fechaMes"
    val fechaMesValue = getFormattedDate(
        sumarDiasAFecha(getFirstDayOfMonth(Date()), 5),
        "yyyy-MM-dd"
    )

    Firebase.database.reference.child(fechaMesPath).setValue(fechaMesValue)
}