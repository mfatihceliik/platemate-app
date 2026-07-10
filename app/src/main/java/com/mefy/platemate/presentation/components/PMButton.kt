package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClick
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PMButtonVariant = PMButtonVariant.Filled,
    enabled: Boolean = true,
    loading: Boolean = false,
    debounceClick: Boolean = true,
    debounceMillis: Long = 600L,
    colors: ButtonColors? = null,
    contentPadding: PaddingValues? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val dims = MaterialTheme.pmDimensions
    val pm = MaterialTheme.pmColors

    val buttonModifier = modifier.fillMaxWidth()
    val shape: Shape = RoundedCornerShape(dims.radius.r10)

    val safeOnClick = if (debounceClick) {
        debouncedClick(debounceMillis = debounceMillis, onClick = onClick)
    } else {
        onClick
    }

    val resolvedColors = colors ?: when (variant) {
        PMButtonVariant.Filled -> ButtonDefaults.buttonColors()
        PMButtonVariant.Tonal -> ButtonDefaults.buttonColors(
            containerColor = pm.primaryContainer,
            contentColor = pm.onPrimaryContainer
        )
        PMButtonVariant.Outlined -> ButtonDefaults.outlinedButtonColors()
        PMButtonVariant.Text -> ButtonDefaults.textButtonColors()
    }

    // Loading'de buton input için disabled edilir; ama gri disabled renklerine
    // düşmemeli, variant renklerini korumalı.
    val displayColors = if (loading) {
        resolvedColors.copy(
            disabledContainerColor = resolvedColors.containerColor,
            disabledContentColor = resolvedColors.contentColor
        )
    } else {
        resolvedColors
    }

    Button(
        onClick = safeOnClick,
        modifier = buttonModifier,
        enabled = enabled && !loading,
        shape = shape,
        colors = displayColors,
        elevation = when (variant) {
            PMButtonVariant.Filled, PMButtonVariant.Tonal -> ButtonDefaults.buttonElevation()
            PMButtonVariant.Outlined, PMButtonVariant.Text -> null
        },
        border = if (variant == PMButtonVariant.Outlined) {
            ButtonDefaults.outlinedButtonBorder(enabled = enabled)
        } else {
            null
        },
        contentPadding = contentPadding ?: when (variant) {
            PMButtonVariant.Text -> ButtonDefaults.TextButtonContentPadding
            else -> ButtonDefaults.ContentPadding
        }
    ) {
        PMButtonContent(
            text = text,
            loading = loading,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

@Composable
private fun PMButtonContent(
    text: String,
    loading: Boolean,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
) {
    val dims = MaterialTheme.pmDimensions

    Box(contentAlignment = Alignment.Center) {
        // Text loading'de görünmez ama ölçülmeye devam eder: buton genişliği sabit kalır.
        Row(
            modifier = Modifier.alpha(if (loading) 0f else 1f),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.invoke()
            PMText(
                text = text,
                style = PMTextStyle.Label,
                color = LocalContentColor.current
            )
            trailingIcon?.invoke()
        }
        if (loading) {
            PMCircularProgressIndicator(
                size = dims.sizing.circleProgressBarXs,
                color = LocalContentColor.current
            )
        }
    }
}

@Preview(name = "PMButton Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMButtonLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMButtonPreviewContent()
    }
}

@Preview(name = "PMButton Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMButtonDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMButtonPreviewContent()
    }
}

@Composable
private fun PMButtonPreviewContent() {

    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.pmColors.background)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        PMButton(
            text = "Filled",
            onClick = {},
            variant = PMButtonVariant.Filled,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Tonal",
            onClick = {},
            variant = PMButtonVariant.Tonal,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Outlined",
            onClick = {},
            variant = PMButtonVariant.Outlined,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Text",
            onClick = {},
            variant = PMButtonVariant.Text,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Loading Filled",
            onClick = {},
            loading = true,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Loading Tonal",
            onClick = {},
            variant = PMButtonVariant.Tonal,
            loading = true,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Loading Outlined",
            onClick = {},
            variant = PMButtonVariant.Outlined,
            loading = true,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Disabled",
            onClick = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
