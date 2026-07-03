package com.mefy.platemate.presentation.features.admin.reporttypes.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun FormField(
    value: String,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    PMTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(name = "FormField", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun FormFieldPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        FormField(value = "SPAM", onValueChange = {})
    }
}