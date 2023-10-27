package com.iue.projectgastosapp.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "gastos")
data class Gasto(
    @PrimaryKey(autoGenerate = true)
    val idGasto: Int = 0,
    val cantidadGasto: Double,
    val fechaGasto: String,
    val descripcionGasto: String,
    val userGasto: String,
    val categoriaGasto: Int
)
