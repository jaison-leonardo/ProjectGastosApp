package com.iue.projectgastosapp.firebase.dataobjects

import androidx.compose.ui.graphics.Color

data class DataProgressBar (
    val label: String,
    val progress: Float,
    val totalValue: Double,
    val colors: List<Color>
)