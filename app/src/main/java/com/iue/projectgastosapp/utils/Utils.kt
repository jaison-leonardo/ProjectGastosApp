package com.iue.projectgastosapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun parseMonetaryValue(value: String): Double {
    // Eliminar caracteres no numéricos y el símbolo de moneda
    val cleanValue = value.replace(Regex("[^\\d.]"), "")
    return cleanValue.toDoubleOrNull() ?: 0.0
}

fun getFormattedDate(date: Date, format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)
}

fun getDateObjFromString(date: String, format: String): Date {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.parse(date)!!
}

fun isDateInRange(currentDate: Date, startDate: Date, endDate: Date): Boolean {
    return currentDate in startDate..endDate
}

fun getFirstDayOfMonth(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

fun getLastDayOfMonth(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    return calendar.time
}

fun sumarDiasAFecha(fecha: Date, dias: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = fecha
    calendar.add(Calendar.DAY_OF_MONTH, dias)
    return calendar.time
}