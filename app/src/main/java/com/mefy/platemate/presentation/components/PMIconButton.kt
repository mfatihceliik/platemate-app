package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.bounceClick
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

private const val DisabledContainerAlpha = 0.12f

@Composable
fun PMIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PMIconButtonVariant = PMIconButtonVariant.Ghost,
    enabled: Boolean = true,
    size: Dp = PMTheme.sizing.iconMd,
    iconColor: Color? = null,
    containerColor: Color? = null,
    borderColor: Color? = null,
    shape: Shape = PMTheme.shapes.medium,
    contentDescription: String? = null,
    debounceClick: Boolean = true,
    debounceMillis: Long = 600L,
) {
    PMIconButtonBase(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        size = size,
        iconColor = iconColor,
        containerColor = containerColor,
        borderColor = borderColor,
        shape = shape,
        contentDescription = contentDescription,
        debounceClick = debounceClick,
        debounceMillis = debounceMillis,
    ) { tint ->
        PMIcon(
            imageVector = imageVector,
            size = size,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun PMIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PMIconButtonVariant = PMIconButtonVariant.Ghost,
    enabled: Boolean = true,
    size: Dp = PMTheme.sizing.iconMd,
    iconColor: Color? = null,
    containerColor: Color? = null,
    borderColor: Color? = null,
    shape: Shape = PMTheme.shapes.medium,
    contentDescription: String? = null,
    debounceClick: Boolean = true,
    debounceMillis: Long = 600L,
) {
    PMIconButtonBase(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        size = size,
        iconColor = iconColor,
        containerColor = containerColor,
        borderColor = borderColor,
        shape = shape,
        contentDescription = contentDescription,
        debounceClick = debounceClick,
        debounceMillis = debounceMillis,
    ) { tint ->
        PMIcon(
            painter = painter,
            size = size,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun PMIconButtonBase(
    onClick: () -> Unit,
    modifier: Modifier,
    variant: PMIconButtonVariant,
    enabled: Boolean,
    size: Dp,
    iconColor: Color?,
    containerColor: Color?,
    borderColor: Color?,
    shape: Shape,
    contentDescription: String?,
    debounceClick: Boolean,
    debounceMillis: Long,
    icon: @Composable (tint: Color) -> Unit,
) {
    val spacing = PMTheme.spacing
    val stroke = PMTheme.stroke
    val colors = PMTheme.colors


    val iconTint = when {
        !enabled -> colors.disabled
        iconColor != null -> iconColor
        else -> when (variant) {
            PMIconButtonVariant.Ghost -> colors.primary
            PMIconButtonVariant.Filled -> colors.onPrimary
            PMIconButtonVariant.Tonal -> colors.onPrimaryContainer
            PMIconButtonVariant.Outlined -> colors.primary
        }
    }

    val resolvedContainerColor = when (variant) {
        PMIconButtonVariant.Ghost,
        PMIconButtonVariant.Outlined -> Color.Transparent
        PMIconButtonVariant.Filled,
        PMIconButtonVariant.Tonal -> when {
            !enabled -> colors.disabled.copy(alpha = DisabledContainerAlpha)
            containerColor != null -> containerColor
            variant == PMIconButtonVariant.Filled -> colors.primary
            else -> colors.primaryContainer
        }
    }

    val resolvedBorderColor = if (variant == PMIconButtonVariant.Outlined) {
        if (enabled) borderColor ?: colors.outline else colors.disabled
    } else {
        null
    }

    val minSize = if (variant == PMIconButtonVariant.Ghost) {
        size
    } else {
        size + spacing.s4 * 2
    }

    Box(
        modifier = modifier
            .defaultMinSize(minSize, minSize)
            .clip(shape)
            .background(resolvedContainerColor)
            .then(
                if (resolvedBorderColor != null) {
                    Modifier.border(
                        stroke.st1, resolvedBorderColor, shape
                    )
                } else {
                    Modifier
                }
            )
            .bounceClick(
                enabled = enabled,
                role = Role.Button,
                onClickLabel = contentDescription,
                debounceMillis = debounceMillis,
                onClick = onClick
            ), contentAlignment = Alignment.Center
    ) {
        icon(iconTint)
    }
}

@Preview(name = "PMIconButton Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMIconButtonLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMIconButtonPreviewContent()
    }
}

@Preview(name = "PMIconButton Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMIconButtonDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMIconButtonPreviewContent()
    }
}

@Composable
private fun PMIconButtonPreviewContent() {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(spacing.s24), verticalArrangement = Arrangement.spacedBy(spacing.s24)
    ) {
        PMText(
            text = "Ghost",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )
        PMIconButtonRow(icon = Icons.Filled.Edit, variant = PMIconButtonVariant.Ghost)

        PMText(
            text = "Filled",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )
        PMIconButtonRow(icon = Icons.Filled.Add, variant = PMIconButtonVariant.Filled)

        PMText(
            text = "Tonal",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )
        PMIconButtonRow(icon = Icons.Filled.Favorite, variant = PMIconButtonVariant.Tonal)

        PMText(
            text = "Outlined",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )
        PMIconButtonRow(icon = Icons.Filled.Close, variant = PMIconButtonVariant.Outlined)
    }
}

@Composable
private fun PMIconButtonRow(icon: ImageVector, variant: PMIconButtonVariant) {

    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMIconButton(onClick = {}, variant = variant, size = sizing.iconXs, imageVector = icon)
        PMIconButton(onClick = {}, variant = variant, size = sizing.iconSm, imageVector = icon)
        PMIconButton(onClick = {}, variant = variant, size = sizing.iconMd, imageVector = icon)
        PMIconButton(onClick = {}, variant = variant, size = sizing.iconLg, imageVector = icon)
        PMIconButton(
            onClick = {},
            variant = variant,
            size = sizing.iconXl,
            enabled = false,
            imageVector = icon,
        )
    }
}
