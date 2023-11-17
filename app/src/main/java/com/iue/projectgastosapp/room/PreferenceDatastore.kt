package com.iue.projectgastosapp.room

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iue.projectgastosapp.dataStore
import kotlinx.coroutines.flow.map

class PreferenceDatastore(context: Context) {
    private var pref = context.dataStore
    companion object {
        // Datos de usuario
        val isLogged = booleanPreferencesKey("isLogged")
        val email = stringPreferencesKey("email")
        val name = stringPreferencesKey("name")
        val lastName = stringPreferencesKey("lastName")
        val id = stringPreferencesKey("id")

        // Preferencias de notificación
        val notificationActivated = booleanPreferencesKey("notificationActivated")
        val umbralMaxGasto = stringPreferencesKey("umbralMaxGasto")
        val notificacionFrecuencia = stringPreferencesKey("notificacionFrecuencia")

        // preferencias de seguridad
        val authAlternativa = booleanPreferencesKey("authAlternativa")
    }

    suspend fun saveData(preferencesData: PreferencesData) {
        pref.edit {
            // Datos de usuario
            it[isLogged] = preferencesData.isLogged
            it[email] = preferencesData.email
            it[name] = preferencesData.name
            it[lastName] = preferencesData.lastName
            it[id] = preferencesData.id
            // Preferencias de notificación
            it[notificationActivated] = preferencesData.notificationActivated
            it[umbralMaxGasto] = preferencesData.umbralMaxGasto
            it[notificacionFrecuencia] = preferencesData.notificacionFrecuencia
            // preferencias de seguridad
            it[authAlternativa] = preferencesData.authAlternativa
        }
    }

    fun getData() = pref.data.map {
        PreferencesData(
            // Datos de usuario
            it[isLogged] ?: false,
            it[email] ?: "",
            it[name] ?: "",
            it[lastName] ?: "",
            it[id] ?: "",
            // Preferencias de notificación
            it[notificationActivated] ?: false,
            it[umbralMaxGasto] ?: "0",
            it[notificacionFrecuencia] ?: "Diaria",
            // preferencias de seguridad
            it[authAlternativa] ?: false,
        )
    }


}