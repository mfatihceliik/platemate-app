package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
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
    isSuccess: Boolean = false,
    supportingText: String? = null,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val dims = MaterialTheme.pmDimensions
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val shouldShowError = isError || errorText != null
    val supportingOrError = errorText ?: supportingText

    val borderColor = when {
        shouldShowError -> MaterialTheme.colorScheme.error
        isSuccess -> MaterialTheme.colorScheme.primary
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    val backgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(modifier = modifier) {
        if (label != null) {
            PMText(
                text = label,
                style = PMTextStyle.Caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 2.dp, bottom = 7.dp)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(dims.sizing.ctaHeightLarge),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            textStyle = TextStyle(
                color = textColor,
                fontSize = 15.sp,
                letterSpacing = if (visualTransformation != VisualTransformation.None) 4.sp else 0.sp
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dims.sizing.ctaHeightLarge)
                        .background(backgroundColor, RoundedCornerShape(14.dp))
                        .border(dims.stroke.st1, borderColor, RoundedCornerShape(14.dp))
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(modifier = Modifier.width(11.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder != null) {
                            PMText(
                                text = placeholder,
                                style = PMTextStyle.Body,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                        innerTextField()
                    }

                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(11.dp))
                        trailingIcon()
                    }
                }
            }
        )

        if (supportingOrError != null) {
            PMText(
                text = supportingOrError,
                style = PMTextStyle.Caption,
                color = if (shouldShowError) MaterialTheme.colorScheme.error else MaterialTheme.pmColors.success,
                modifier = Modifier.padding(start = 2.dp, top = 7.dp)
            )
        }
    }
}

@Preview(name = "PMTextField Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMTextFieldLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTextFieldPreviewContent()
    }
}

@Preview(name = "PMTextField Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMTextFieldDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMTextFieldPreviewContent()
    }
}

@Composable
private fun PMTextFieldPreviewContent() {
    var normal by remember { mutableStateOf("ahmet_yl") }
    var email by remember { mutableStateOf("ahmet@ornek.com") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.pmDimensions.spacing.s16)
    ) {
        PMTextField(
            value = normal,
            onValueChange = { normal = it },
            label = "Kullanıcı Adı",
            supportingText = "Kullanıcı adı müsait",
            isSuccess = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        PMTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-posta",
            placeholder = "name@domain.com",
            isError = true,
            errorText = "E-posta veya şifre hatalı",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
