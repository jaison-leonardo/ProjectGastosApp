package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.enums.getCategoryById
import com.iue.projectgastosapp.firebase.dataobjects.DataMetaAhorro
import com.iue.projectgastosapp.utils.getDateObjFromString
import com.iue.projectgastosapp.utils.getFirstDayOfMonth
import com.iue.projectgastosapp.utils.getLastDayOfMonth
import com.iue.projectgastosapp.utils.isDateInRange
import java.util.Date

fun getMetasByUser(idUser: String, callback: (ArrayList<DataMetaAhorro>?, String) -> Unit) {
    val dbReference = Firebase.database.reference.child("users").child(idUser)
        .child("metas")

    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val metasList = ArrayList<DataMetaAhorro>()
                for (metaSnapshot in snapshot.children) {
                    val fechaMes = metaSnapshot.child("fechaMes").value.toString()
                    val fechaMesObj = getDateObjFromString(fechaMes, "yyyy-MM-dd")
                    val fechaMesMin = getFirstDayOfMonth(Date())
                    val fechaMesMax = getLastDayOfMonth(Date())
                    val dateIsRange = isDateInRange(fechaMesObj, fechaMesMin, fechaMesMax)
                    if (dateIsRange) {
                        for (item in metaSnapshot.children) {
                            if (item.key.toString() != "fechaMes") {
                                val id = item.child("categoriaMeta").value.toString()
                                val categoriaMeta = getCategoryById(id)?.label
                                val meta = DataMetaAhorro(
                                    categoriaMeta = categoriaMeta!!,
                                    montoMeta = item.child("montoMeta").value.toString()
                                        .toDouble(),
                                    fechaLimite = item.child("fechaLimite").value.toString()
                                )
                                metasList.add(meta)
                            }
                        }
                        callback(metasList, "Metas encontradas")
                    }
                }

            } else {
                callback(arrayListOf(), "No se encontraron metas")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null, "Error al obtener las metas")
        }
    })
}