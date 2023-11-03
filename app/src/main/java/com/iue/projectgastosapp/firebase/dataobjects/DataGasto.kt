package com.iue.projectgastosapp.firebase.dataobjects

data class DataGasto(
    val cantidadGasto: Double,
    val fechaGasto: String,
    val descripcionGasto: String,
    val categoriaGasto: String
) {
    constructor() : this(0.0, "", "", "")
}
// {"1": {"cantidadGasto": 25000.0, "fechaGasto": "2021-10-10", "descripcionGasto": "Compra de comida", "categoriaGasto": "1"},
// "2": {"cantidadGasto": 3200.0, "fechaGasto": "2021-10-11", "descripcionGasto": "Transporte", "categoriaGasto": "2"}}
// {"cantidadGasto": 3200.0, "fechaGasto": "2021-10-11", "descripcionGasto": "Transporte", "categoriaGasto": "2"}