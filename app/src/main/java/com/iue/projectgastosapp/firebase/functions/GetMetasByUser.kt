package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.Objects
import com.iue.projectgastosapp.enums.getCategoryById
import com.iue.projectgastosapp.firebase.dataobjects.DataMetaAhorro
import com.iue.projectgastosapp.utils.getDateObjFromString
import com.iue.projectgastosapp.utils.getFirstDayOfMonth
import com.iue.projectgastosapp.utils.getLastDayOfMonth
import com.iue.projectgastosapp.utils.isDateInRange
import java.util.Date

fun getMetasByUser(idUser: String, callback: (ArrayList<DataMetaAhorro>?, String) -> Unit) {
    val dbReference = Firebase.database.reference
        .child(Objects.USUARIOS.label)
        .child(idUser)
        .child(Objects.METAS.label)

    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val metasList = ArrayList<DataMetaAhorro>()
                snapshot.children.forEach { metaSnapshot ->
                    val fechaMes = metaSnapshot.child("fechaMes").value.toString()
                    val fechaMesObj = getDateObjFromString(fechaMes, "yyyy-MM-dd")
                    val fechaMesMin = getFirstDayOfMonth(Date())
                    val fechaMesMax = getLastDayOfMonth(Date())

                    if (isDateInRange(fechaMesObj, fechaMesMin, fechaMesMax)) {
                        metaSnapshot.children
                            .filter { it.key != "fechaMes" }
                            .mapTo(metasList) { item ->
                                val id = item.child("categoriaMeta").value.toString()
                                val categoriaMeta = getCategoryById(id)?.label
                                DataMetaAhorro(
                                    categoriaMeta = categoriaMeta!!,
                                    montoMeta = item.child("montoMeta").value.toString().toDouble(),
                                    fechaLimite = item.child("fechaLimite").value.toString()
                                )
                            }
                    }
                }
                callback(metasList.ifEmpty { null }, "Metas encontradas")
            } else {
                callback(arrayListOf(), "No se encontraron metas")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "Error al obtener las metas")
        }
    })
}
