package com.iue.projectgastosapp.views.composable.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatisticCard() {
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.bodyMedium
                .copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Card(
            modifier = Modifier
                .clickable { /* Acción al hacer clic en la tarjeta */ }
                .padding(16.dp)
        ) {
            val data = listOf(
                PieChartData("Entretenimiento", Color(0xFF109619), 25f),
                PieChartData("Transporte", Color(0xFF3366cc), 30f),
                PieChartData("Alimentación", Color(0xFFdc3912), 20f),
                PieChartData("Otros", Color(0xFFff9900), 25f)
            )
            PieChartStatistics(dataChart = data)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun StatisticCardPreview() {
    StatisticCard()
}