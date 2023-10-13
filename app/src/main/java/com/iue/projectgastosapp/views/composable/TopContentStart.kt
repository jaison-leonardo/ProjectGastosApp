package com.iue.projectgastosapp.views.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.R

@Composable
fun TopContentStart() {
    // Parte superior con color y logo centrado
    Box(
        modifier = Modifier
            .background(color = Color(0xFF2196F3))
            .fillMaxWidth()
            .height(320.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_se),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}