package com.iue.projectgastosapp.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val idCategoria: Int = 0,
    val nombreCategoria: String,
    val colorCategoria: String
)
