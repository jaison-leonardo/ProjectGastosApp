package com.iue.projectgastosapp.views.sescreens

import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.enums.Categories
import com.iue.projectgastosapp.firebase.functions.createMetaByUser
import com.iue.projectgastosapp.firebase.dataobjects.DataMetaAhorro
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.getIdByUserAndDate
import com.iue.projectgastosapp.firebase.functions.getMetasByUser
import com.iue.projectgastosapp.utils.getFormattedDate
import com.iue.projectgastosapp.utils.getLastDayOfMonth
import com.iue.projectgastosapp.utils.isDateInRange
import com.iue.projectgastosapp.utils.parseMonetaryValue
import com.iue.projectgastosapp.utils.sumarDiasAFecha
import com.iue.projectgastosapp.views.composable.CardExpandable
import com.iue.projectgastosapp.views.composable.ShowDialog
import me.thekusch.composeview.CurrencyTextField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun SavingsGoalsScreen(dataUser: DataUser) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    val categoriesList =
        listOf(
            Categories.EMPTY,
            Categories.ALIMENTACION,
            Categories.TRANSPORTE,
            Categories.ENTRETENIMIENTO,
            Categories.OTROS
        )
    var selectedCategory by remember { mutableStateOf(categoriesList[0].label) }
    var selectedCategoriaObject by remember { mutableStateOf(Categories.EMPTY) }
    var montoCurrencyField by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    var isFechaLimiteVisible by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaLimite by remember { mutableStateOf(Calendar.getInstance()) }
    var itemsList by remember { mutableStateOf<List<DataMetaAhorro>>(emptyList()) }
    val listState = rememberLazyListState()
    var updateData by remember { mutableStateOf(false) }

    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    getMetasByUser(dataUser.id) { metasAhorro, messageMetas ->
        if (metasAhorro != null) {
            itemsList = metasAhorro
            updateData = true
        } else {
            message = messageMetas
            showDialog = true
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Text(text = "Nueva meta de ahorro", fontWeight = FontWeight.Bold)
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Monto $", fontWeight = FontWeight.Bold)
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
                        .clickable { isFechaLimiteVisible = true }
                        .padding(16.dp)
                ) {
                    Text(
                        text = dateFormat.format(fechaLimite.time),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                }

                if (isFechaLimiteVisible) {
                    val onDateSetListener =
                        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            fechaLimite.set(year, month, dayOfMonth)
                            isFechaLimiteVisible = false
                        }
                    focusManager.clearFocus()
                    DatePickerDialog(
                        context,
                        onDateSetListener,
                        fechaLimite.get(Calendar.YEAR),
                        fechaLimite.get(Calendar.MONTH),
                        fechaLimite.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
                Spacer(modifier = Modifier.size(30.dp))
                // DropDown categoría
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
                // Botón "Registrar gasto"
                Button(
                    onClick = {
                        val validateFields = montoCurrencyField.text.isNotEmpty() &&
                                parseMonetaryValue(montoCurrencyField.text) > 0.0 &&
                                selectedCategoriaObject != Categories.EMPTY
                        val fechaSelect = fechaLimite.time
                        val fechaMin = sumarDiasAFecha(Date(), 1)
                        val fechaMax = getLastDayOfMonth(fechaLimite.time)
                        val validateDate = isDateInRange(fechaSelect, fechaMin, fechaMax)
                        if (validateFields && validateDate) {
                            for (item in itemsList) {
                                if (item.categoriaMeta == selectedCategoriaObject.label) {
                                    message =
                                        "Ya existe una meta de ahorro para la categoría seleccionada"
                                    showDialog = true
                                    break
                                }
                            }
                            if (!showDialog) {
                                getIdByUserAndDate(dataUser.id, fechaSelect) { idObject, _ ->
                                    if (idObject != null) {
                                        val meta = DataMetaAhorro(
                                            selectedCategoriaObject.id,
                                            parseMonetaryValue(montoCurrencyField.text),
                                            getFormattedDate(fechaSelect, "yyyy-MM-dd")
                                        )
                                        createMetaByUser(
                                            dataUser.id,
                                            idObject,
                                            meta,
                                            selectedCategoriaObject.key
                                        ) { _, messageCreate ->
                                            message = messageCreate
                                            showDialog = true
                                            getMetasByUser(dataUser.id) { metasAhorro, messageMetas ->
                                                if (metasAhorro != null) {
                                                    itemsList = metasAhorro
                                                    updateData = true
                                                } else {
                                                    message = messageMetas
                                                    showDialog = true
                                                }
                                            }
                                        }
                                    } else {
                                        message = "Ha ocurrido un error buscando las metas"
                                        showDialog = true
                                    }
                                }
                            }
                        } else {
                            val messageDate =
                                "La fecha de registro debe ser mayor a la fecha actual y menor o igual al último día del mes"
                            val messageFields = "Todos los campos son obligatorios"
                            message = if (validateDate) messageFields else messageDate
                            message =
                                if (!validateFields && !validateDate) "$messageDate\n$messageFields" else message
                            showDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
                ) {
                    Row {
                        Text(text = "Agregar Meta", color = Color.White)
                        Spacer(modifier = Modifier.size(5.dp))
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                // Lista de metas
                Spacer(modifier = Modifier.size(30.dp))
                Text(text = "Metas Actuales", fontWeight = FontWeight.Bold)
            }
        }
        items(items = itemsList, itemContent = { item ->
            CardExpandable(
                title = item.categoriaMeta,
                content = "\tMeta: ${item.montoMeta} \n\tFecha límite: ${item.fechaLimite}"
            )
        })
    }
    LaunchedEffect(listState) {
        listState.scrollToItem(0)
        updateData = false
    }
    ShowDialog(
        show = showDialog,
        message = message,
        onDismiss = { showDialog = false },
        onButtonClick = { showDialog = false }
    )
}
