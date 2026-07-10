package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.variant.PMTextFieldVariant
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
    variant: PMTextFieldVariant = PMTextFieldVariant.Outlined,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
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
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    PMTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        isError = isError,
        isSuccess = isSuccess,
        supportingText = supportingText,
        errorText = errorText,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation
    )
}

@Composable
fun PMTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    variant: PMTextFieldVariant = PMTextFieldVariant.Outlined,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
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
    val colors = MaterialTheme.pmColors
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val shouldShowError = isError || errorText != null
    val supportingOrError = errorText ?: supportingText

    val borderColor = when {
        shouldShowError -> colors.error
        isSuccess -> colors.primary
        isFocused -> colors.primary
        else -> colors.outlineVariant
    }

    val backgroundColor = when (variant) {
        PMTextFieldVariant.Chat -> colors.surfaceVariant
        PMTextFieldVariant.Search -> colors.searchFieldBg
        else -> colors.surface
    }
    val textColor = colors.onSurface

    val isChat = variant == PMTextFieldVariant.Chat
    val isSearch = variant == PMTextFieldVariant.Search

    val heightModifier: Modifier = when {
        isSearch -> Modifier.height(dims.sizing.searchBarHeight)
        isChat -> Modifier.heightIn(min = dims.sizing.chatFieldMinHeight)
        singleLine -> Modifier.height(dims.sizing.ctaHeightLarge)
        else -> Modifier
            .heightIn(min = dims.sizing.ctaHeightLarge)
            .fillMaxHeight()
    }

    Column(modifier = modifier) {
        if (label != null) {
            PMSectionLabel(
                text = label,
                modifier = Modifier.padding(
                    bottom = dims.spacing.s4
                )
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(heightModifier),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine && !isChat,
            maxLines = maxLines,
            textStyle = TextStyle(
                color = textColor,
                fontSize = dims.fontSize.lg,
                letterSpacing = if (visualTransformation != VisualTransformation.None) 4.sp else 0.sp
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(colors.primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(heightModifier)
                        .background(
                            backgroundColor,
                            when (variant) {
                                PMTextFieldVariant.Chat -> RoundedCornerShape(dims.radius.rFull)
                                PMTextFieldVariant.Search -> RoundedCornerShape(dims.radius.r16)
                                else -> RoundedCornerShape(dims.radius.r16)
                            }
                        )
                        .then(
                            if (isChat) Modifier
                            else Modifier
                                .border(
                                    width = dims.stroke.st2,
                                    color = borderColor,
                                    shape = when (variant) {
                                        PMTextFieldVariant.Search -> RoundedCornerShape(dims.radius.r16)
                                        else -> RoundedCornerShape(dims.radius.r12)
                                    }
                                )
                        )
                        .padding(
                            horizontal = dims.spacing.s16,
                            vertical = if (isChat || isSearch) dims.spacing.s10 else dims.spacing.s12
                        ),
                    verticalAlignment = if (singleLine || isChat) Alignment.CenterVertically else Alignment.Top
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(modifier = Modifier.width(dims.spacing.s12))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.text.isEmpty() && placeholder != null) {
                            PMText(
                                text = placeholder,
                                style = PMTextStyle.Body,
                                fontSize = dims.fontSize.lg,
                                color = colors.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                        innerTextField()
                    }

                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(dims.spacing.s12))
                        trailingIcon()
                    }
                }
            }
        )

        if (supportingOrError != null) {
            PMSectionLabel(
                text = supportingOrError,
                style = PMTextStyle.Label,
                color = if (shouldShowError) colors.error else colors.success,
                modifier = Modifier.padding(
                    start = dims.spacing.s4,
                    bottom = dims.spacing.s4
                )
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
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    var normal by remember { mutableStateOf("ahmet_yl") }
    var email by remember { mutableStateOf("ahmet@ornek.com") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s16)
    ) {
        PMTextField(
            value = normal,
            onValueChange = { normal = it },
            label = "Kullanıcı Adı",
            supportingText = "Kullanıcı adı müsait",
            isSuccess = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dims.spacing.s16))

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
