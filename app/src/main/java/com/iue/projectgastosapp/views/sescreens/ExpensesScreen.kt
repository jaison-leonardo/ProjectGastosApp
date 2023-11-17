package com.iue.projectgastosapp.views.sescreens

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.enums.getPresupuestoByCategory
import com.iue.projectgastosapp.firebase.dataobjects.DataGasto
import com.iue.projectgastosapp.firebase.dataobjects.DataPresupuesto
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.dataobjects.GastosAndCategoria
import com.iue.projectgastosapp.firebase.functions.createGastoByUser
import com.iue.projectgastosapp.firebase.functions.getBudgetByUserAndDate
import com.iue.projectgastosapp.firebase.functions.getGastosByUser
import com.iue.projectgastosapp.firebase.functions.getIdObjectByUser
import com.iue.projectgastosapp.firebase.functions.getMetasByUser
import com.iue.projectgastosapp.firebase.functions.getSumatoriaGastosByDateAndCategoria
import com.iue.projectgastosapp.utils.getFormattedDate
import com.iue.projectgastosapp.utils.parseMonetaryValue
import com.iue.projectgastosapp.views.composable.ShowDialog
import me.thekusch.composeview.CurrencyTextField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun ExpensesScreen(dataUser: DataUser) {
    val categoriesList =
        listOf(
            Categories.EMPTY,
            Categories.ALIMENTACION,
            Categories.TRANSPORTE,
            Categories.ENTRETENIMIENTO,
            Categories.OTROS
        )
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categoriesList[0].label) }
    var selectedCategoriaObject by remember { mutableStateOf(Categories.EMPTY) }
    var montoCurrencyField by remember { mutableStateOf(TextFieldValue("")) }
    var fechaGasto by remember { mutableStateOf(Calendar.getInstance()) }
    var descripcion by remember { mutableStateOf("") }

    val context = LocalContext.current
    var isDatePickerDialogVisible by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var presupuesto by remember {
        mutableStateOf(
            DataPresupuesto(0.0, 0.0, 0.0, 0.0, "")
        )
    }
    var gastosPorCategoria by remember { mutableStateOf<List<GastosAndCategoria>>(emptyList()) }
    var totalMeta by remember { mutableStateOf(0.0) }
    LaunchedEffect(dataUser.id) {
        getBudgetByUserAndDate(dataUser.id, Date()) { presupuestoData, messagePresupuesto ->
            if (presupuestoData != null && messagePresupuesto == "budgetId") {
                presupuesto = presupuestoData
            } else if (presupuestoData == null) {
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
        val categorias = listOf(
            Categories.ALIMENTACION,
            Categories.TRANSPORTE,
            Categories.ENTRETENIMIENTO,
            Categories.OTROS
        )
        getSumatoriaGastosByDateAndCategoria(dataUser.id, categorias) { sumatoria, messageCat ->
            if (sumatoria != null) {
                gastosPorCategoria = sumatoria
            } else {
                showDialog = true
                message = messageCat
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Dropdown de categoría
        Text(text = "Categoría", fontWeight = FontWeight.Bold)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedCategory,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Gray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                categoriesList.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.label) },
                        onClick = {
                            selectedCategory = selectionOption.label
                            selectedCategoriaObject = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        // Campo de monto
        Text(text = "Monto", fontWeight = FontWeight.Bold)
        CurrencyTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused },
            onChange = {
                montoCurrencyField = TextFieldValue(it)
            },
            currencySymbol = "$",
            errorColor = Color.Red,
            locale = Locale.getDefault(),
            initialText = "0",
            maxNoOfDecimal = 0,
            errorText = "El formato no es correcto",
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.size(30.dp))
        // Selector de fecha
        Text(text = "Fecha", fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .clickable { isDatePickerDialogVisible = true }
                .padding(16.dp)
        ) {
            Text(
                text = dateFormat.format(fechaGasto.time),
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
        }

        if (isDatePickerDialogVisible) {
            val onDateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    fechaGasto.set(year, month, dayOfMonth)
                    isDatePickerDialogVisible = false
                }
            DatePickerDialog(
                context,
                onDateSetListener,
                fechaGasto.get(Calendar.YEAR),
                fechaGasto.get(Calendar.MONTH),
                fechaGasto.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        Spacer(modifier = Modifier.size(30.dp))
        // Campo de descripción
        Text(text = "Descripción", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .height(100.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.size(30.dp))
        // Botón "Registrar gasto"
        Button(
            onClick = {
                val validateFields =
                    montoCurrencyField.text.isNotEmpty() && parseMonetaryValue(montoCurrencyField.text) > 0.0
                            && descripcion.isNotEmpty() && selectedCategoriaObject != Categories.EMPTY
                if (validateFields) {
                    var createGasto = false
                    val presupuestoByCategoria = getPresupuestoByCategory(
                        presupuesto,
                        selectedCategoriaObject
                    )
                    if (gastosPorCategoria.isEmpty() && presupuestoByCategoria > 0.0) {
                        createGasto = true
                    } else if (presupuestoByCategoria > 0.0) {
                        val categoriaSeleccionada = gastosPorCategoria.firstOrNull {
                            it.categoriaId == selectedCategoriaObject.id
                        }

                        if (categoriaSeleccionada != null) {
                            val totalGasto = categoriaSeleccionada.gastosSum + parseMonetaryValue(montoCurrencyField.text)

                            if (totalGasto <= presupuestoByCategoria) {
                                createGasto = true
                            } else {
                                message = "Se ha excedido el presupuesto de la categoría"
                                showDialog = true
                            }
                        } else {
                            message = "No existe la categoria seleccionada :o"
                            showDialog = true
                        }
                    } else {
                        message = "No ha definido ningún presupuesto para esta categoría"
                        showDialog = true
                    }
                    if (createGasto) {
                        getIdObjectByUser(dataUser.id, "Gastos") { idGasto, responseId ->
                            if (idGasto != null) {
                                createGastoByUser(
                                    dataUser.id,
                                    idGasto,
                                    DataGasto(
                                        cantidadGasto = parseMonetaryValue(montoCurrencyField.text),
                                        fechaGasto = getFormattedDate(
                                            fechaGasto.time,
                                            "yyyy-MM-dd"
                                        ),
                                        descripcionGasto = descripcion,
                                        categoriaGasto = selectedCategoriaObject.id
                                    )
                                ) { isSuccessful, response ->
                                    if (isSuccessful) {
                                        selectedCategory = categoriesList[0].label
                                        selectedCategoriaObject = Categories.EMPTY
                                        montoCurrencyField = TextFieldValue("0")
                                        descripcion = ""
                                        fechaGasto = Calendar.getInstance()
                                    }
                                    message = response
                                    showDialog = true
                                    getGastosByUser(dataUser.id) { gastosList, responseGet ->
                                        Log.i("Gasto_Register", gastosList.toString())
                                        Log.i("Gasto_Register_response", responseGet)
                                    }
                                }
                            } else {
                                message = responseId
                                showDialog = true
                            }
                        }
                    }
                } else {
                    message = "Todos los campos son obligatorios"
                    showDialog = true
                }
            },

            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
        ) {
            Row {
                Text(text = "Registrar gasto", color = Color.White)
                Spacer(modifier = Modifier.size(5.dp))
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
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


