package com.iue.projectgastosapp.views.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShowCircularIndicator(show: Boolean) {
    if (!show) return
    MyCircularProgress()
}

@Composable
private fun MyCircularProgress() = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.5f))
) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(80.dp)
            .wrapContentSize(Alignment.Center)
            .align(Alignment.Center)
    )
}