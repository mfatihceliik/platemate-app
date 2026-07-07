package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun PMPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorText: String? = null,
    showToggle: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val colors = MaterialTheme.pmColors
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    PMTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorText = errorText,
        leadingIcon = leadingIcon,
        singleLine = true,
        trailingIcon = if (showToggle) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    PMIcon(
                        imageVector = if (passwordVisible) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        },
                        contentDescription = if (passwordVisible) {
                            stringResource(R.string.common_hide)
                        } else {
                            stringResource(R.string.common_show)
                        },
                        tint = MaterialTheme.pmColors.primary
                    )
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
        visualTransformation = if (showToggle && passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@Preview(name = "PMPasswordField Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPasswordFieldLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMPasswordFieldPreviewContent()
    }
}

@Preview(name = "PMPasswordField Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMPasswordFieldDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMPasswordFieldPreviewContent()
    }
}

@Composable
private fun PMPasswordFieldPreviewContent() {
    var password by remember { mutableStateOf("123456") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.pmColors.background)
            .padding(MaterialTheme.pmDimensions.spacing.s16)
    ) {
        PMPasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Enter password",
            modifier = Modifier.fillMaxWidth()
        )
        PMPasswordField(
            value = "12",
            onValueChange = {},
            label = "Confirm Password",
            isError = true,
            errorText = "Password is too short",
            showToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.pmDimensions.spacing.s12)
        )
    }
}
