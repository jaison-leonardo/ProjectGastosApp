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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iue.projectgastosapp.firebase.dataobjects.DataMetaAhorro
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.getMetasByUser
import com.iue.projectgastosapp.views.composable.ShowDialog

@Composable
fun SavingGoalCard(dataUser: DataUser) {
    var metas by remember { mutableStateOf<List<DataMetaAhorro>>(emptyList()) }
    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    getMetasByUser(dataUser.id) { metaList, messageMetas ->
        if (metaList != null) {
            metas = metaList
        } else {
            showDialog = true
            message = messageMetas
        }

    }
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
                CustomProgressBar(
                    label = "Transporte",
                    progress = 25f,
                    totalValue = metas.find { it.categoriaMeta == "Transporte" }?.montoMeta ?: 0.0,
                    colors = listOf(Color(0xFF2196F3), Color(0xFF03A9F4))
                )
                CustomProgressBar(
                    label = "Entretenimiento",
                    progress = 50f,
                    totalValue = metas.find { it.categoriaMeta == "Entretenimiento" }?.montoMeta ?: 0.0,
                    colors = listOf(Color(0xFF0F9D58), Color(0xF055CA4D))
                )
                CustomProgressBar(
                    label = "Alimentación",
                    progress = 75f,
                    totalValue = metas.find { it.categoriaMeta == "Alimentación" }?.montoMeta ?: 0.0,
                    colors = listOf(Color(0xFFF44336), Color(0xFFE91E63))
                )
                CustomProgressBar(
                    label = "Otros",
                    progress = 100f,
                    totalValue = metas.find { it.categoriaMeta == "Otros" }?.montoMeta ?: 0.0,
                    colors = listOf(Color(0xFFFFC107), Color(0xFFFF9800))
                )
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
