package com.iue.projectgastosapp.views.composable

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.iue.projectgastosapp.utils.parseMonetaryValue

@Composable
fun PercentageTextField(value: String, onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done,
    )
    val keyboardActions = KeyboardActions(
        onDone = {
            focusManager.clearFocus()
        }
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = PercentageVisualTransformation(),
        colors = TextFieldDefaults.textFieldColors(
            textColor = LocalContentColor.current.copy(alpha = 1f),
            cursorColor = LocalContentColor.current
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    focusManager.clearFocus()
                    focusManager.moveFocus(FocusDirection.Down)
                    return@onKeyEvent true
                }
                return@onKeyEvent false
            }
    )
}

class PercentageVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val newText = buildAnnotatedString {
            val cleanValue = parseMonetaryValue(text.text)
            if (cleanValue != 0.0) {
                append("${cleanValue.toInt()} %")
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset.coerceIn(0, newText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset.coerceIn(0, text.length)
            }

        }
        return TransformedText(newText, offsetMapping)
    }
}