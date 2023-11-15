package com.iue.projectgastosapp.firebase.dataobjects

data class DataPresupuesto(
    val alimentacion: Double = 0.0,
    val transporte: Double = 0.0,
    val entretenimiento: Double = 0.0,
    val otros: Double = 0.0,
    val fechaPresupuesto: String = "",
)
