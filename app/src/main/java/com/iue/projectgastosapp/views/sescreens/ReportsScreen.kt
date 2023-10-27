package com.iue.projectgastosapp.views.sescreens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.views.composable.CheckboxListColumn
import com.iue.projectgastosapp.views.composable.OptionCheckItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ReportsScreen() {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val context = LocalContext.current
    val fechaInicial by remember { mutableStateOf(Calendar.getInstance()) }
    var isFechaInicialVisible by remember { mutableStateOf(false) }

    val fechaFinal by remember { mutableStateOf(Calendar.getInstance()) }
    var isFechaIFinalVisible by remember { mutableStateOf(false) }

    val categories = listOf(
        Categories.ALIMENTACION.label,
        Categories.TRANSPORTE.label,
        Categories.ENTRETENIMIENTO.label,
        Categories.OTROS.label
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            // Rango de fechas
            Text(text = "Rango de fechas", fontWeight = FontWeight.Bold)
            Column(
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Fecha inicial", fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier
                        .border(1.dp, Color(0xFF202221), shape = RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp))
                        .clickable { isFechaInicialVisible = true }
                        .padding(16.dp)) {
                        Text(
                            text = dateFormat.format(fechaInicial.time),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                    }
                    if (isFechaInicialVisible) {
                        val onDateInicialSetListener =
                            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                                fechaInicial.set(year, month, dayOfMonth)
                                isFechaInicialVisible = false
                            }
                        DatePickerDialog(
                            context,
                            onDateInicialSetListener,
                            fechaInicial.get(Calendar.YEAR),
                            fechaInicial.get(Calendar.MONTH),
                            fechaInicial.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(text = "Fecha final", fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier
                        .border(
                            1.dp, Color(0xFF202221), shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(15.dp))
                        .clickable { isFechaIFinalVisible = true }
                        .padding(16.dp)) {
                        Text(
                            text = dateFormat.format(fechaFinal.time),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                    }
                    if (isFechaIFinalVisible) {
                        val onDateFinalSetListener =
                            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                                fechaFinal.set(year, month, dayOfMonth)
                                isFechaIFinalVisible = false
                            }
                        DatePickerDialog(
                            context,
                            onDateFinalSetListener,
                            fechaFinal.get(Calendar.YEAR),
                            fechaFinal.get(Calendar.MONTH),
                            fechaFinal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }
            }
            Spacer(modifier = Modifier.size(30.dp))
        }
        item {
            // Categorias
            Text(text = "Categorias", fontWeight = FontWeight.Bold)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                val options = categories.map { it ->
                    val checked = remember { mutableStateOf(false) }
                    OptionCheckItem(
                        label = it,
                        checked = checked.value,
                        onCheckedChange = { checked.value = it }
                    )
                }
                CheckboxListColumn(options = options)
            }
            Spacer(modifier = Modifier.size(30.dp))
        }
        // Botón "Generar Reporte"
        item {
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
            ) {
                Row {
                    Text(text = "Generar Reporte", color = Color.White)
                    Spacer(modifier = Modifier.size(5.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
    ReportsScreen()
}