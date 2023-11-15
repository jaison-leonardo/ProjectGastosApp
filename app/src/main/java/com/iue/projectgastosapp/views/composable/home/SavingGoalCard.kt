package com.iue.projectgastosapp.views.composable.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import com.iue.projectgastosapp.viewmodel.DataModelHome
import com.iue.projectgastosapp.views.composable.ShowDialog

@Composable
fun SavingGoalCard(viewModel: DataModelHome) {
    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        Text(
            text = "Metas de ahorro",
            style = MaterialTheme.typography.bodyMedium
                .copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Card(
            modifier = Modifier
                .clickable { /* Acción al hacer clic en la tarjeta */ }
                .padding(16.dp)
        ) {
            Column {
                viewModel.metas.forEach {
                    CustomProgressBar(
                        label = it.label,
                        progress = it.progress,
                        totalValue = it.totalValue,
                        colors = it.colors
                    )
                }
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
