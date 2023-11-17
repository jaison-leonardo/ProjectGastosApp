package com.iue.projectgastosapp.views.composable

import com.github.mikephil.charting.formatter.ValueFormatter

class CustomValueFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "$value %"
    }
}