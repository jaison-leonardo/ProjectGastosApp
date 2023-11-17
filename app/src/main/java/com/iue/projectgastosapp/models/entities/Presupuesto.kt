package com.iue.projectgastosapp.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "presupuesto")
data class Presupuesto(
    @PrimaryKey(autoGenerate = true)
    val idPresupuesto: Int = 0,
    @ColumnInfo(name = "cantidad_presupuesto")
    val cantidadPresupuesto: Double,
    @ColumnInfo(name = "cantidad_actual_presupuesto")
    val cantidadActualPresupuesto: Double,
    @ColumnInfo(name = "fecha_inicio_presupuesto")
    val fechaInicioPresupuesto: String,
    @ColumnInfo(name = "fecha_fin_presupuesto")
    val fechaFinPresupuesto: String,
    @ColumnInfo(name = "estado_presupuesto")
    val estadoPresupuesto: String,
    @ColumnInfo(name = "user_presupuesto")
    val userPresupuesto: String,
    @ColumnInfo(name = "categoria_gasto")
    val categoriaGasto: Int
)
