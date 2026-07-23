package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.ui.semantics.Role
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.bounceClick
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PMButtonVariant = PMButtonVariant.Filled,
    enabled: Boolean = true,
    loading: Boolean = false,
    debounceMillis: Long = 600L,
    buttonColors: ButtonColors? = null,
    contentPadding: PaddingValues? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {

    val sizing = PMTheme.sizing
    val colors = PMTheme.colors
    val shape = PMTheme.shapes.medium

    val buttonModifier = modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = sizing.buttonMinHeight)
    val padding = contentPadding ?: when (variant) {
        PMButtonVariant.Text -> ButtonDefaults.TextButtonContentPadding
        else -> ButtonDefaults.ContentPadding
    }

    val isActuallyEnabled = enabled && !loading

    val resolvedColors = buttonColors ?: when (variant) {
        PMButtonVariant.Filled -> ButtonDefaults.buttonColors()
        PMButtonVariant.Tonal -> ButtonDefaults.buttonColors(
            containerColor = colors.primaryContainer,
            contentColor = colors.onPrimaryContainer
        )
        PMButtonVariant.Outlined -> ButtonDefaults.outlinedButtonColors()
        PMButtonVariant.Text -> ButtonDefaults.textButtonColors()
    }

    val displayColors = if (loading) {
        resolvedColors.copy(
            disabledContainerColor = resolvedColors.containerColor,
            disabledContentColor = resolvedColors.contentColor
        )
    } else {
        resolvedColors
    }

    val border = if (variant == PMButtonVariant.Outlined) {
        ButtonDefaults.outlinedButtonBorder(enabled = enabled)
    } else null

    val animatedContainerColor by animateColorAsState(
        targetValue = displayColors.containerColor,
        label = "containerColor"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = displayColors.contentColor,
        label = "contentColor"
    )

    Box(
        modifier = buttonModifier
            .clip(shape)
            .background(animatedContainerColor)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .bounceClick(
                enabled = isActuallyEnabled,
                debounceMillis = debounceMillis,
                role = Role.Button,
                onClick = onClick
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides animatedContentColor
        ) {
            PMButtonContent(
                text = text,
                loading = loading,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon
            )
        }
    }
}

@Composable
private fun PMButtonContent(
    text: String,
    loading: Boolean,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing

    Box(contentAlignment = Alignment.Center) {
        // Text loading'de görünmez ama ölçülmeye devam eder: buton genişliği sabit kalır.
        Row(
            modifier = Modifier.alpha(if (loading) 0f else 1f),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8),
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
                size = sizing.circleProgressBarXs,
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

    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s12)
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
