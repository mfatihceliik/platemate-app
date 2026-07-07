package com.mefy.platemate.presentation.features.admin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun AddLanguageRow(
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    var locale by remember { mutableStateOf("") }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMTextField(
            value = locale,
            onValueChange = { if (it.length <= 3) locale = it.lowercase().filter(Char::isLetter) },
            placeholder = "Dil (tr, en, de)",
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        PMButton(
            text = "Ekle",
            onClick = {
                if (locale.isNotBlank()) {
                    onAdd(locale)
                    locale = ""
                }
            },
            enabled = locale.isNotBlank()
        )
    }
}
