package com.iue.projectgastosapp.enums

import com.iue.projectgastosapp.firebase.dataobjects.DataPresupuesto

enum class Categories(val id: String, val label: String, val key: String) {
    EMPTY("0","Seleccionar categoría", "empty"),
    ALIMENTACION("1","Alimentación", "alimentacion"),
    TRANSPORTE("2","Transporte", "transporte"),
    ENTRETENIMIENTO("3","Entretenimiento", "entretenimiento"),
    OTROS("4","Otros", "otros"),
}

fun getCategoryById(id: String): Categories? {
    return Categories.values().find { it.id == id }
}

fun getPresupuestoByCategory(dataPresupuesto: DataPresupuesto, category: Categories): Double {
    return when (category) {
        Categories.ALIMENTACION -> dataPresupuesto.alimentacion
        Categories.TRANSPORTE -> dataPresupuesto.transporte
        Categories.ENTRETENIMIENTO -> dataPresupuesto.entretenimiento
        Categories.OTROS -> dataPresupuesto.otros
        else -> 0.0
    }
}