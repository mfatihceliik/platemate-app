package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMChip(
    modifier: Modifier = Modifier,
    label: String,
    style: PMChipStyle = PMChipStyle.Soft,
    accentColor: Color = PMTheme.colors.primary,
    selected: Boolean = false,
    count: Int? = null,
    leadingIcon: ImageVector? = null,
    dense: Boolean = false,
    enabled: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null,
    borderColor: Color? = null,
    onClick: (() -> Unit)? = null,
) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val fontSize = PMTheme.fontSize
    val stroke = PMTheme.stroke
    val shape = PMTheme.shapes.medium


    val resolved = remember(
        accentColor, style, selected, containerColor, contentColor, borderColor
    ) {
        resolveChipColors(
            accent = accentColor,
            style = style,
            selected = selected,
            containerOverride = containerColor,
            contentOverride = contentColor,
            borderOverride = borderColor,
        )
    }

    val interactive = onClick != null && enabled

    var boxModifier = modifier
        .clip(shape)
        .background(resolved.container)
        .border(stroke.st1, resolved.border, shape)
    if (!dense) boxModifier = boxModifier.heightIn(min = sizing.chipHeight)
    if (interactive) boxModifier = boxModifier.debouncedClickable(onClick = onClick)
    boxModifier = if (dense) {
        boxModifier.padding(horizontal = spacing.s8, vertical = spacing.s4)
    } else {
        boxModifier.padding(horizontal = if (interactive) spacing.s16 else spacing.s8)
    }

    val textFontSize = if (dense) fontSize.sm else fontSize.md

    Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                PMIcon(
                    imageVector = leadingIcon,
                    tint = resolved.content,
                    size = sizing.iconSm
                )
            }
            PMText(
                text = label,
                style = if (dense) PMTextStyle.Caption else PMTextStyle.Body,
                fontSize = textFontSize,
                fontWeight = FontWeight.SemiBold,
                color = resolved.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (count != null) {
                PMText(
                    text = count.toString(),
                    fontSize = textFontSize,
                    color = resolved.content.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// Color resolution (pure — no Composable/theme access, safe to remember)
// ─────────────────────────────────────────────────────────────────

enum class PMChipStyle {
    Soft,
    Solid,
    Outline
}
private data class ResolvedChipColors(
    val container: Color,
    val content: Color,
    val border: Color,
)

private fun resolveChipColors(
    accent: Color,
    style: PMChipStyle,
    selected: Boolean,
    containerOverride: Color?,
    contentOverride: Color?,
    borderOverride: Color?,
): ResolvedChipColors {
    val effectiveStyle = if (selected) PMChipStyle.Solid else style
    val (container, content, border) = when (effectiveStyle) {
        PMChipStyle.Soft -> Triple(
            accent.copy(alpha = 0.12f),
            accent,
            accent.copy(alpha = 0.30f),
        )
        PMChipStyle.Solid -> {
            val onAccent = if (accent.luminance() > 0.179f) Color(0xFF1E293B) else Color.White
            Triple(accent, onAccent, accent)
        }
        PMChipStyle.Outline -> Triple(
            Color.Transparent,
            accent,
            accent.copy(alpha = 0.30f),
        )
    }
    return ResolvedChipColors(
        container = containerOverride ?: container,
        content = contentOverride ?: content,
        border = borderOverride ?: border,
    )
}

// ─────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────

@Preview(name = "PMChip Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMChipLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMChipPreviewContent()
    }
}

@Preview(name = "PMChip Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun PMChipDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMChipPreviewContent()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PMChipPreviewContent() {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    var selected by remember { mutableStateOf(setOf(0, 2)) }
    val tags = listOf("Nazik", "Yol verdi", "Saygılı", "Sabırlı")

    FlowRow(
        modifier = Modifier.padding(spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(spacing.s8),
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        // Soft display badge with count
        PMChip(label = "Nazik", count = 42, style = PMChipStyle.Soft)
        // Soft with backend-style accent color
        PMChip(label = "Hızlı", style = PMChipStyle.Soft, accentColor = colors.warning)
        // Solid with leading icon
        PMChip(label = "Onaylı", style = PMChipStyle.Solid, leadingIcon = Icons.Filled.CheckCircle)
        // Explicit color pair (status badge)
        PMChip(
            label = "Beklemede",
            containerColor = colors.categoryOrangeBg,
            contentColor = colors.categoryOrangeFg
        )
        // Dense
        PMChip(
            label = "Kesik",
            style = PMChipStyle.Soft,
            dense = true,
            accentColor = colors.iconStar,
            leadingIcon = Icons.Filled.Star
        )

        // Selectable Outline ↔ Solid toggle
        tags.forEachIndexed { index, tag ->
            PMChip(
                label = tag,
                style = PMChipStyle.Outline,
                selected = index in selected,
                onClick = {
                    selected = if (index in selected) selected - index else selected + index
                }
            )
        }
    }
}
