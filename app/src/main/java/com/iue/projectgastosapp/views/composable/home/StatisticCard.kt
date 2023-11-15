package com.iue.projectgastosapp.views.composable.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.viewmodel.DataModelHome

@Composable
fun StatisticCard(model: DataModelHome, dataUser: DataUser) {
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
                .clickable { model.loadData(dataUser) }
                .padding(16.dp)
        ) {
            PieChartStatistics(dataChart = model.data)
        }
    }


}