package com.iue.projectgastosapp.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meta_ahorro")
data class MetaAhorro(
    @PrimaryKey(autoGenerate = true)
    val idMetaAhorro: Int = 0,
    @ColumnInfo(name = "nombre_meta_ahorro")
    val nombreMetaAhorro: String,
    @ColumnInfo(name = "cantidad_objetivo")
    val cantidadObjetivo: Double,
    @ColumnInfo(name = "cantidad_actual")
    val cantidadActual: Double,
    @ColumnInfo(name = "fecha_inicio_meta_ahorro")
    val fechaInicioMetaAhorro: String,
    @ColumnInfo(name = "fecha_fin_meta_ahorro")
    val fechaFinMetaAhorro: String,
    @ColumnInfo(name = "user_meta_ahorro")
    val userMetaAhorro: String,
    @ColumnInfo(name = "categoria_gasto")
    val categoriaGasto: Int
)
