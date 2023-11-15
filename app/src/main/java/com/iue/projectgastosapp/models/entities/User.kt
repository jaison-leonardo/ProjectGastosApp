package com.iue.projectgastosapp.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val idUser: String,
    val name: String,
    val lastName: String,
    val email: String
)
