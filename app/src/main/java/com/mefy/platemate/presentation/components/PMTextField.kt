package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val supportingOrError = errorText ?: supportingText
    val shouldShowError = isError || errorText != null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        isError = shouldShowError,
        label = label?.let { labelText -> { Text(text = labelText) } },
        placeholder = placeholder?.let { placeholderText -> { Text(text = placeholderText) } },
        supportingText = supportingOrError?.let { helper ->
            { Text(text = helper) }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation
    )
}

@Preview(name = "PMTextField Light", showBackground = true, backgroundColor = 0xFFF6FAFB)
@Composable
private fun PMTextFieldLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTextFieldPreviewContent()
    }
}

@Preview(name = "PMTextField Dark", showBackground = true, backgroundColor = 0xFF101618)
@Composable
private fun PMTextFieldDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMTextFieldPreviewContent()
    }
}

@Composable
private fun PMTextFieldPreviewContent() {
    var normal by remember { mutableStateOf("fatih") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.pmDimensions.spacing.s16)
    ) {
        PMTextField(
            value = normal,
            onValueChange = { normal = it },
            label = "Username",
            supportingText = "Must be at least 3 characters",
            modifier = Modifier.fillMaxWidth()
        )

        PMTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "name@domain.com",
            isError = true,
            errorText = "Email format is invalid",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.pmDimensions.spacing.s12)
        )

        PMTextField(
            value = "Disabled input",
            onValueChange = {},
            label = "State",
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.pmDimensions.spacing.s12)
        )
    }
}
