package com.iue.projectgastosapp.firebase.functions

import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.enums.getCategoryById
import com.iue.projectgastosapp.firebase.dataobjects.DataMonth
import com.iue.projectgastosapp.utils.getDateObjFromString
import com.iue.projectgastosapp.utils.getMonthNameByDate
import com.iue.projectgastosapp.utils.isDateInRange

fun getExpensesByDateRangeAndCategory(
    idUser: String,
    dateRange: Pair<String, String>,
    categories: List<Categories>,
    callback: (List<DataMonth>?, String) -> Unit
) {
    getGastosByUser(idUser) { gastosList, message ->
        if (gastosList != null) {
            val startDate = getDateObjFromString(dateRange.first, "yyyy-MM-dd")
            val endDate = getDateObjFromString(dateRange.second, "yyyy-MM-dd")

            // Filtrar gastos por rango de fechas y categorías
            val gastosByDateRangeAndCategory = gastosList.filter { gasto ->
                val gastoDate = getDateObjFromString(gasto.fechaGasto, "yyyy-MM-dd")
                isDateInRange(gastoDate, startDate, endDate) && categories.any { it.id == gasto.categoriaGasto }
            }

            // Crear un mapa para almacenar la suma de gastos por mes y categoría
            val sumByMonthAndCategory = mutableMapOf<String, MutableMap<String, Double>>()

            // Calcular la suma de gastos por mes y categoría
            for (gasto in gastosByDateRangeAndCategory) {
                val month = getMonthNameByDate(gasto.fechaGasto)
                val category = getCategoryById(gasto.categoriaGasto)?.label

                sumByMonthAndCategory
                    .getOrPut(month) { mutableMapOf() }
                    .merge(category!!, gasto.cantidadGasto, Double::plus)
            }

            // Crear la lista final de DataMonth
            val result = sumByMonthAndCategory.map { (month, categorySums) ->
                DataMonth(
                    month = month,
                    entertainment = categorySums.getOrDefault("Entretenimiento", 0.0),
                    transportation = categorySums.getOrDefault("Transporte", 0.0),
                    food = categorySums.getOrDefault("Alimentación", 0.0),
                    others = categorySums.getOrDefault("Otros", 0.0)
                )
            }

            // Añadir la fila Total al final
            val total = DataMonth(
                month = "Total",
                entertainment = result.sumOf { it.entertainment },
                transportation = result.sumOf { it.transportation },
                food = result.sumOf { it.food },
                others = result.sumOf { it.others }
            )
            callback(result.plus(total), "Reporte generado correctamente")
        } else {
            callback(null, message)
        }
    }
}