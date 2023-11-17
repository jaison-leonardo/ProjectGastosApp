package com.iue.projectgastosapp.views.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class OptionCheckItem(
    var checked: Boolean,
    var onCheckedChange: (Boolean) -> Unit = {},
    val label: String,
    var enabled: Boolean = true
)

@Composable
fun CheckboxListColumn(options: List<OptionCheckItem>) {
    Column {
        Spacer(Modifier.size(16.dp))
        options.forEach { option ->
            LabelledCheckbox(
                checked = option.checked,
                onCheckedChange = option.onCheckedChange,
                label = option.label,
                enabled = option.enabled,
                spacerWidth = 32
            )
        }
    }
}

@Composable
fun CheckboxListRow(options: List<OptionCheckItem>) {
    Row {
        Spacer(Modifier.size(16.dp))
        options.forEach { option ->
            Spacer(Modifier.size(16.dp))
            LabelledCheckbox(
                checked = option.checked,
                onCheckedChange = option.onCheckedChange,
                label = option.label,
                enabled = option.enabled,
                spacerWidth = 6
            )
        }
    }
}

@Composable
fun LabelledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    spacerWidth: Int,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = colors
        )
        Spacer(Modifier.width(spacerWidth.dp))
        Text(label)
    }
}