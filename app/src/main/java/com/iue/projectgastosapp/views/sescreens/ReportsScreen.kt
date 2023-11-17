package com.iue.projectgastosapp.views.sescreens

import android.Manifest
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.iue.projectgastosapp.R
import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.getExpensesByDateRangeAndCategory
import com.iue.projectgastosapp.utils.generatePDF
import com.iue.projectgastosapp.utils.getFormattedDate
import com.iue.projectgastosapp.views.composable.ShowCircularIndicator
import com.iue.projectgastosapp.views.composable.ShowDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReportsScreen(dataUser: DataUser) {
    val permisoReadState =
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val permisoWriteState =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val context = LocalContext.current
    val fechaInicial by remember { mutableStateOf(Calendar.getInstance()) }
    var isFechaInicialVisible by remember { mutableStateOf(false) }

    val fechaFinal by remember { mutableStateOf(Calendar.getInstance()) }
    var isFechaIFinalVisible by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var showCircularIndicator by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current

    val categories = listOf(
        Categories.ALIMENTACION,
        Categories.TRANSPORTE,
        Categories.ENTRETENIMIENTO,
        Categories.OTROS
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
        // Botón "Generar Reporte"
        item {
            Button(
                onClick = {
                    showCircularIndicator = true
                    lifecycleOwner.lifecycleScope.launch {
                        permisoWriteState.launchPermissionRequest()
                        permisoReadState.launchPermissionRequest()

                    }
                    if (permisoReadState.status.isGranted && permisoWriteState.status.isGranted) {
                        val dateRangePair = Pair(
                            getFormattedDate(fechaInicial.time, "yyyy-MM-dd"),
                            getFormattedDate(fechaFinal.time, "yyyy-MM-dd")
                        )
                        getExpensesByDateRangeAndCategory(
                            dataUser.id,
                            dateRangePair,
                            categories
                        ) { dataMonthList, messageGet ->
                            showCircularIndicator = false
                            if (dataMonthList != null) {
                                val dateStart = getFormattedDate(fechaInicial.time, "dd/MM/yyyy")
                                val dateEnd = getFormattedDate(fechaFinal.time, "dd/MM/yyyy")
                                val title = "Reporte de gastos del $dateStart al $dateEnd"
                                val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_se_64)
                                generatePDF(context, title, logoBitmap, dataMonthList)

                            } else {
                                showDialog = true
                                message = messageGet
                            }
                        }
                    } else if (permisoReadState.status.shouldShowRationale || permisoWriteState.status.shouldShowRationale) {
                        message =
                            "Se necesita permisos para generar un reporte, " +
                                    "para usar esta funcionalidad por favor acepte los permisos"
                        showDialog = true
                        permisoWriteState.launchPermissionRequest()
                        permisoReadState.launchPermissionRequest()
                    } else {
                        showDialog = true
                        message = "No se pudo generar el reporte"
                    }
                    showCircularIndicator = false
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
    ShowCircularIndicator(show = showCircularIndicator)
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
}

@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
//    ReportsScreen()
}