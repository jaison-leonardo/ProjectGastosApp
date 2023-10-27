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
import com.iue.projectgastosapp.firebase.dataobjects.DataPresupuesto
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.getBudgetByUserAndDate
import com.iue.projectgastosapp.firebase.functions.getMetasByUser
import com.iue.projectgastosapp.views.composable.ShowDialog
import java.util.Date

@Composable
fun CurrentBudgetCard(dataUser: DataUser) {
    var presupuesto by remember {
        mutableStateOf(
            DataPresupuesto(0.0, 0.0, 0.0, 0.0, "")
        )
    }
    var showPresupuesto by remember { mutableStateOf(false) }
    var totalPresupuesto by remember { mutableStateOf(0.0) }
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var totalMeta by remember { mutableStateOf(0.0) }
    getBudgetByUserAndDate(dataUser.id, Date()) { presupuestoData, messagePresupuesto ->
        if (presupuestoData != null) {
            showPresupuesto = true
            presupuesto = presupuestoData
        } else {
            showDialog = true
            message = messagePresupuesto
        }
    }
    getMetasByUser(dataUser.id) { metaList, messageMetas ->
        if (metaList != null) {
            metaList.forEach {
                totalMeta += it.montoMeta
            }
        } else {
            showDialog = true
            message = messageMetas
        }

    }
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
                totalPresupuesto = if (showPresupuesto) {
                    ExpenseItem("Alimentación", presupuesto.alimentacion)
                    ExpenseItem("Transporte", presupuesto.transporte)
                    ExpenseItem("Entretenimiento", presupuesto.entretenimiento)
                    ExpenseItem("Otros", presupuesto.otros)
                    presupuesto.alimentacion + presupuesto.transporte + presupuesto.entretenimiento + presupuesto.otros
                } else {
                    ExpenseItem("Alimentación", 0.0)
                    ExpenseItem("Transporte", 0.0)
                    ExpenseItem("Entretenimiento", 0.0)
                    ExpenseItem("Otros", 0.0)
                    0.0
                }


                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                TotalRow(totalMeta, totalPresupuesto)
            }
        }
    }
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
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

// Extension function to format currency
fun Double.formatToCurrency(): String {
    return String.format("$%,.0f", this)
}
