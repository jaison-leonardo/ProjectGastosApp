package com.iue.projectgastosapp.views.sescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.firebase.functions.checkIfDateExists
import com.iue.projectgastosapp.firebase.functions.createBudgetByUser
import com.iue.projectgastosapp.firebase.dataobjects.DataPresupuesto
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.firebase.functions.getIdObjectByUser
import com.iue.projectgastosapp.utils.getFormattedDate
import com.iue.projectgastosapp.utils.parseMonetaryValue
import com.iue.projectgastosapp.views.composable.ShowDialog
import me.thekusch.composeview.CurrencyTextField
import java.util.Date
import java.util.Locale

@Composable
fun BudgetScreen(dataUser: DataUser) {
    var alimentacionCurrencyField by remember { mutableStateOf(TextFieldValue("0")) }
    var transporteCurrencyField by remember { mutableStateOf(TextFieldValue("0")) }
    var entretenimientoCurrencyField by remember { mutableStateOf(TextFieldValue("0")) }
    var otrosCurrencyField by remember { mutableStateOf(TextFieldValue("0")) }

    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Spacer(modifier = Modifier.size(30.dp))
            Text(text = "Alimentación", fontWeight = FontWeight.Bold)
            CurrencyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray),
                onChange = {
                    alimentacionCurrencyField = TextFieldValue(it)
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
        }
        item {
            Spacer(modifier = Modifier.size(30.dp))
            Text(text = "Transporte", fontWeight = FontWeight.Bold)
            CurrencyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray),
                onChange = {
                    transporteCurrencyField = TextFieldValue(it)
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
        }
        item {
            Spacer(modifier = Modifier.size(30.dp))
            Text(text = "Entretenimiento", fontWeight = FontWeight.Bold)
            CurrencyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray),
                onChange = {
                    entretenimientoCurrencyField = TextFieldValue(it)
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
        }
        item {
            Spacer(modifier = Modifier.size(30.dp))
            Text(text = "Otros", fontWeight = FontWeight.Bold)
            CurrencyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray),
                onChange = {
                    otrosCurrencyField = TextFieldValue(it)
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
        }
        item {
            Spacer(modifier = Modifier.size(30.dp))
            // Botón "Registrar gasto"
            Button(
                onClick = {
                    val currentDate = Date()
                    checkIfDateExists(
                        userId = dataUser.id,
                        currentDate,
                        callback = { exists, messageCheck ->
                            if (!exists) {
                                getIdObjectByUser(
                                    dataUser.id,
                                    "presupuestos"
                                ) { idGasto, responseId ->
                                    if (idGasto != null) {
                                        createBudgetByUser(
                                            dataUser.id, idGasto, DataPresupuesto(
                                                parseMonetaryValue(alimentacionCurrencyField.text),
                                                parseMonetaryValue(transporteCurrencyField.text),
                                                parseMonetaryValue(entretenimientoCurrencyField.text),
                                                parseMonetaryValue(otrosCurrencyField.text),
                                                getFormattedDate(currentDate, "yyyy-MM-dd")
                                            )
                                        ) { _, messageCreate ->
                                            message = messageCreate
                                            showDialog = true
                                        }
                                    } else {
                                        message = responseId
                                        showDialog = true
                                    }
                                }
                            } else {
                                showDialog = true
                                val messageError = "Ha ocurrido un error buscando los presupuestos"
                                message = if (messageCheck != "") messageCheck else messageError
                            }
                        })
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1876D0))
            ) {
                Row {
                    Text(text = "Registrar cambios", color = Color.White)
                    Spacer(modifier = Modifier.size(5.dp))
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
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
