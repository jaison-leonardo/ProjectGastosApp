package com.iue.projectgastosapp.states

data class DataNotification(
    var notificacionActivada: Boolean = false,
    var umbralMaxGasto: String = "0",
    var notificacionFrecuencia: String = "0"
)
