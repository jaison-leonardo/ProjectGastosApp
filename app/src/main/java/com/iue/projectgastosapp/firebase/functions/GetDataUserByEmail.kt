package com.iue.projectgastosapp.firebase.functions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iue.projectgastosapp.firebase.dataobjects.DataUser

fun getDataUserByEmail(email: String, callback: (DataUser?, String) -> Unit) {
    // Ir a firebase y obtener los datos del usuario
    val dbReference = Firebase.database.reference.child("users")
    dbReference.orderByChild("email").equalTo(email)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val idUser = snapshot.children.first().key.toString()
                    val userSnapshot = snapshot.child(idUser)
                    callback(
                        DataUser(
                            id = idUser,
                            name = userSnapshot.child("name").value.toString(),
                            lastName = userSnapshot.child("lastName").value.toString(),
                            email = userSnapshot.child("email").value.toString()
                        ),
                        "Usuario encontrado"
                    )
                } else {
                    callback(null, "Usuario no encontrado")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, "Error al obtener los datos del usuario DatabaseError")
            }
        })
}