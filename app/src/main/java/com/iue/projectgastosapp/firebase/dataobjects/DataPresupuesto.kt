package com.iue.projectgastosapp.firebase.dataobjects

data class DataPresupuesto(
    val alimentacion: Double,
    val transporte: Double,
    val entretenimiento: Double,
    val otros: Double,
    val fechaPresupuesto: String,
)
