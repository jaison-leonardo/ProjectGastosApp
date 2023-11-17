package com.iue.projectgastosapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.enums.getCategoryById
import com.iue.projectgastosapp.firebase.dataobjects.DataMetaAhorro
import com.iue.projectgastosapp.firebase.dataobjects.DataPresupuesto
import com.iue.projectgastosapp.firebase.dataobjects.DataProgressBar
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.getBudgetByUserAndDate
import com.iue.projectgastosapp.firebase.functions.getMetasByUser
import com.iue.projectgastosapp.firebase.functions.getSumatoriaGastosByDateAndCategoria
import com.iue.projectgastosapp.utils.formatToCurrency
import com.iue.projectgastosapp.views.composable.home.PieChartData
import java.util.Date
import kotlin.math.round

class DataModelHome : ViewModel() {
    // Variables para almacenar datos
    var presupuesto: DataPresupuesto by mutableStateOf(DataPresupuesto())
    var totalMeta: Double by mutableStateOf(0.0)
    var data: List<PieChartData> by mutableStateOf(emptyList())
    var metas: List<DataProgressBar> by mutableStateOf(emptyList())
    var showLoading: Boolean by mutableStateOf(false)
    var showDialog: Boolean by mutableStateOf(false)
    var message: String by mutableStateOf("")

    // Función para cargar datos
    fun loadData(dataUser: DataUser) {
        showLoading = true
        getBudgetByUserAndDate(dataUser.id, Date()) { presupuestoData, messagePresupuesto ->
            if (presupuestoData != null && messagePresupuesto == "budgetId") {
                val categorias = listOf(
                    Categories.ALIMENTACION,
                    Categories.TRANSPORTE,
                    Categories.ENTRETENIMIENTO,
                    Categories.OTROS
                )
                getSumatoriaGastosByDateAndCategoria(dataUser.id, categorias) { sumatoria, messageCat ->
                    if (sumatoria != null) {
                        val restante: Map<String, Double> = sumatoria.associateBy({
                            it.categoriaId
                        }, {
                            when (it.categoriaId) {
                                Categories.ALIMENTACION.id -> presupuestoData.alimentacion - it.gastosSum
                                Categories.TRANSPORTE.id -> presupuestoData.transporte - it.gastosSum
                                Categories.ENTRETENIMIENTO.id -> presupuestoData.entretenimiento - it.gastosSum
                                Categories.OTROS.id -> presupuestoData.otros - it.gastosSum
                                else -> 0.0
                            }
                        })
                        val totalAndRestante = restante.toMutableMap()
                        totalAndRestante["total"] =
                            presupuestoData.alimentacion + presupuestoData.transporte + presupuestoData.entretenimiento + presupuestoData.otros
                        val porcentajeTotal: Map<String, Double> = totalAndRestante
                            .filterKeys { it != "total" }
                            .mapValues { (_, value) ->
                                (value * 100) / (totalAndRestante["total"] ?: 1.0)
                            }
                        val porcentajeRestante: Map<String, Double> = totalAndRestante.keys
                            .filter { it != "total" }.associateWith { categoryId ->
                                val restanteCategoria = restante[categoryId] ?: 0.0
                                val porcentajeTotalCategoria = porcentajeTotal[categoryId] ?: 0.0
                                val presupuestoCategoria = totalAndRestante[categoryId] ?: 1.0

                                (restanteCategoria * porcentajeTotalCategoria) / presupuestoCategoria
                            }
                        val dataTemp: List<PieChartData> =
                            porcentajeRestante.map { (categoryId, porcentaje) ->
                                val label =
                                    getCategoryById(categoryId)?.label
                                val restanteCategoria = restante[categoryId] ?: 0.0

                                PieChartData(
                                    value = (round(porcentaje * 10) / 10.0).toFloat(),
                                    label = label!!,
                                    detail = "${restanteCategoria.formatToCurrency()} restantes"
                                )
                            }
                        data = dataTemp
                        getMetasByUser(dataUser.id) { metaList, messageMetas ->
                            if (metaList != null) {
                                val mapColors = mutableMapOf<String, List<Color>>()
                                mapColors["1"] = listOf(Color(0xFFF44336), Color(0xFFE91E63))
                                mapColors["2"] = listOf(Color(0xFF2196F3), Color(0xFF03A9F4))
                                mapColors["3"] = listOf(Color(0xFF0F9D58), Color(0xF055CA4D))
                                mapColors["4"] = listOf(Color(0xFFFFC107), Color(0xFFFF9800))
                                val metasAhorro = metaList.map { meta ->
                                    val categoria = getCategoryById(meta.categoriaMeta)
                                    val colors = mapColors[meta.categoriaMeta] ?: emptyList()
                                    DataProgressBar(
                                        label = categoria?.label ?: "",
                                        progress = getProgressByRestante(restante, meta),
                                        totalValue = meta.montoMeta,
                                        colors = colors
                                    )
                                }
                                metas = metasAhorro
                                showLoading = false
                            } else {
                                showDialog = true
                                message = messageMetas
                            }
                        }
                    } else {
                        showDialog = true
                        message = messageCat
                    }
                }
                presupuesto = presupuestoData
            } else {
                showLoading = false
                showDialog = true
                message = messagePresupuesto
            }
        }
        getMetasByUser(dataUser.id) { metaList, messageMetas ->
            if (metaList != null) {
                metaList.forEach {
                    totalMeta += it.montoMeta
                }
            } else {
                showDialog = true
                message = messageMetas
            }
        }
    }
}

fun getProgressByRestante(
    restante: Map<String, Double>,
    meta: DataMetaAhorro
): Float {
    restante.forEach {
        if (it.key == meta.categoriaMeta) {
            return if ((it.value - meta.montoMeta) >= 0) {
                100f
            } else {
                val porcentaje = (it.value * 100) / meta.montoMeta
                (round(porcentaje * 10) / 10.0).toFloat()
            }
        }
    }
    return 0.0f
}