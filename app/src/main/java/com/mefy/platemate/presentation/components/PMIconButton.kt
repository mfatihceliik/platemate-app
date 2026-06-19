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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.clickable
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.components.model.PMIconButtonVariant
import com.mefy.platemate.presentation.components.util.debouncedClick
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PMIconButtonVariant = PMIconButtonVariant.Ghost,
    enabled: Boolean = true,
    size: Dp = MaterialTheme.pmDimensions.sizing.iconMd,
    shape: Shape = MaterialTheme.shapes.small,
    contentDescription: String? = null,
    debounceClick: Boolean = true,
    debounceMillis: Long = 600L,
    content: @Composable () -> Unit,
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val primary = MaterialTheme.pmColors.primary

    val safeOnClick = if (debounceClick) {
        debouncedClick(debounceMillis = debounceMillis, onClick = onClick)
    } else {
        onClick
    }

    val (bgColor, iconTint, borderColor) = when (variant) {
        PMIconButtonVariant.Ghost -> Triple(
            Color.Transparent,
            if (enabled) MaterialTheme.pmColors.textPrimary else MaterialTheme.pmColors.disabled,
            Color.Transparent,
        )
        PMIconButtonVariant.Filled -> Triple(
            if (enabled) primary else MaterialTheme.pmColors.disabled,
            Color.White,
            Color.Transparent,
        )
        PMIconButtonVariant.Tonal -> Triple(
            if (enabled) MaterialTheme.pmColors.secondaryContainer else MaterialTheme.pmColors.surfaceVariant,
            if (enabled) MaterialTheme.pmColors.onSecondaryContainer else MaterialTheme.pmColors.disabled,
            Color.Transparent,
        )
        PMIconButtonVariant.Outlined -> Triple(
            Color.Transparent,
            if (enabled) MaterialTheme.pmColors.textPrimary else MaterialTheme.pmColors.disabled,
            if (enabled) MaterialTheme.pmColors.outline else MaterialTheme.pmColors.disabled,
        )
    }

    Box(
        modifier = modifier
            // Minimum dokunma hedefi `size`; içerik (PMIcon) daha büyükse Box büyür,
            // böylece icon boyutu görünür şekilde etki eder (sabit .size() kırpıyordu).
            .defaultMinSize(minWidth = size, minHeight = size)
            .clip(shape)
            .background(bgColor)
            .then(
                if (variant == PMIconButtonVariant.Outlined) {
                    Modifier.border(dims.stroke.st1, borderColor, shape)
                } else {
                    Modifier
                }
            )
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClickLabel = contentDescription,
                onClick = safeOnClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        PMIconButtonIconScope(tint = iconTint, content = content)
    }
}

@Composable
private fun PMIconButtonIconScope(
    tint: Color,
    content: @Composable () -> Unit,
) {
    androidx.compose.runtime.CompositionLocalProvider(
        androidx.compose.material3.LocalContentColor provides tint,
        content = content,
    )
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
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.pmColors.background)
            .padding(dims.spacing.s24),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s24)
    ) {
        PMText(text = "Ghost", style = com.mefy.platemate.presentation.components.model.PMTextStyle.SectionLabel, color = MaterialTheme.pmColors.textTertiary)
        PMIconButtonRow(icon = Icons.Filled.Edit, variant = PMIconButtonVariant.Ghost)

        PMText(text = "Filled", style = com.mefy.platemate.presentation.components.model.PMTextStyle.SectionLabel, color = MaterialTheme.pmColors.textTertiary)
        PMIconButtonRow(icon = Icons.Filled.Add, variant = PMIconButtonVariant.Filled)

        PMText(text = "Tonal", style = com.mefy.platemate.presentation.components.model.PMTextStyle.SectionLabel, color = MaterialTheme.pmColors.textTertiary)
        PMIconButtonRow(icon = Icons.Filled.Share, variant = PMIconButtonVariant.Tonal)

        PMText(text = "Outlined", style = com.mefy.platemate.presentation.components.model.PMTextStyle.SectionLabel, color = MaterialTheme.pmColors.textTertiary)
        PMIconButtonRow(icon = Icons.Filled.Close, variant = PMIconButtonVariant.Outlined)
    }
}

@Composable
private fun PMIconButtonRow(icon: ImageVector, variant: PMIconButtonVariant) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dims = MaterialTheme.pmDimensions
        PMIconButton(onClick = {}, variant = variant, size = dims.sizing.iconSm) {
            PMIcon(imageVector = icon, contentDescription = null, size = dims.sizing.iconSm)
        }
        PMIconButton(onClick = {}, variant = variant, size = dims.sizing.iconMd) {
            PMIcon(imageVector = icon, contentDescription = null, size = dims.sizing.iconMd)
        }
        PMIconButton(onClick = {}, variant = variant, size = dims.sizing.iconLg) {
            PMIcon(imageVector = icon, contentDescription = null, size = dims.sizing.iconLg)
        }
        PMIconButton(onClick = {}, variant = variant, size = dims.sizing.iconXl) {
            PMIcon(imageVector = icon, contentDescription = null, size = dims.sizing.iconXl)
        }
        PMIconButton(onClick = {}, variant = variant, enabled = false) {
            PMIcon(imageVector = icon, contentDescription = null)
        }
    }
}
