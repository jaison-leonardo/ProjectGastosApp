package com.iue.projectgastosapp.views.composable.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomProgressBar(
    label: String,
    progress: Float,
    totalValue: Double,
    colors: List<Color>
) {
    var widthBar by remember { mutableStateOf(0) }
    val screenWidth = LocalDensity.current.run {
        LocalConfiguration.current.screenWidthDp.dp.value
    }
    widthBar = if (screenWidth <= 360) 200 else 250
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color(0xFF202221), shape = RoundedCornerShape(15.dp))
                    .clip(RoundedCornerShape(15.dp))
                    .height(30.dp)
                    .background(Color(0xFFe4e4e4))
                    .width(widthBar.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .height(30.dp)
                        .background(Brush.horizontalGradient(colors))
                        .width(widthBar.dp * progress / 100)
                )
                Text(
                    text = "$progress %",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Text(
                text = totalValue.formatToCurrency(),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CustomProgressBarPreview() {
    CustomProgressBar(
        label = "Entretenimiento",
        progress = 25f,
        totalValue = 100000.0,
        colors = listOf(Color(0xFF0F9D58), Color(0xF055CA4D))
    )
}

