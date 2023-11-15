package com.iue.projectgastosapp.room

data class PreferencesData(
    var isLogged:Boolean = false,
    var email:String = "",
    var name:String = "",
    var lastName:String = "",
    var id:String = "",
    // Preferencias de notificación
    var notificationActivated:Boolean = false,
    var umbralMaxGasto:String = "0",
    var notificacionFrecuencia:String = "Diaria",
    // preferencias de seguridad
    var authAlternativa:Boolean = false
)

