package com.iue.projectgastosapp.views.composable.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iue.projectgastosapp.utils.formatToCurrency
import com.iue.projectgastosapp.viewmodel.DataModelHome

@Composable
fun CurrentBudgetCard(viewModel: DataModelHome) {
    var totalPresupuesto by remember { mutableStateOf(0.0) }
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        Text(
            text = "Presupuesto Actual",
            style = MaterialTheme.typography.bodyMedium
                .copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                ExpenseItem("Alimentación", viewModel.presupuesto.alimentacion)
                ExpenseItem("Transporte", viewModel.presupuesto.transporte)
                ExpenseItem("Entretenimiento", viewModel.presupuesto.entretenimiento)
                ExpenseItem("Otros", viewModel.presupuesto.otros)
                totalPresupuesto =
                    viewModel.presupuesto.alimentacion + viewModel.presupuesto.transporte + viewModel.presupuesto.entretenimiento + viewModel.presupuesto.otros
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                TotalRow(viewModel.totalMeta, totalPresupuesto)
            }
        }
    }
}

@Composable
fun TotalRow(savingsGoal: Double, total: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.align(Alignment.End),
            text = "Total: ${total.formatToCurrency()}", fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.align(Alignment.End),
            text = "Total Meta Ahorro: ${savingsGoal.formatToCurrency()}",
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun ExpenseItem(name: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$name:", fontWeight = FontWeight.Bold)
        Text(text = amount.formatToCurrency())
    }
}

