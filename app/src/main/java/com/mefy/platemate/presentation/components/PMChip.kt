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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.components.model.PMChipStyle
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Unified chip / badge. Replaces the former `PMBadge` (display badge) and
 * `PMTagChip` (selectable chip).
 *
 * Coloring is fully dynamic and never hardcoded: all fills, borders and text
 * derive from a single [accentColor] (theme `primary` by default, or a
 * backend-provided hex via [com.mefy.platemate.presentation.common.hexToColor]).
 * Explicit [containerColor] / [contentColor] / [borderColor] win over derivation
 * for the rare callsite that needs an exact color pair (e.g. status badges).
 *
 * Color precedence (highest → lowest): explicit overrides → [selected] (forces a
 * [PMChipStyle.Solid] look) → [style].
 *
 * @param selected when true, renders the Solid (filled accent) look regardless of [style].
 * @param count optional trailing number (e.g. tag occurrence count).
 * @param dense compact variant for grids — smaller text and padding, no min height.
 * @param onClick `null` makes the chip a non-interactive display badge (no ripple).
 */
@Composable
fun PMChip(
    modifier: Modifier = Modifier,
    label: String,
    style: PMChipStyle = PMChipStyle.Soft,
    accentColor: Color = MaterialTheme.pmColors.primary,
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
    val dims = MaterialTheme.pmDimensions

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

    val shape = remember(dims.radius.r12) { RoundedCornerShape(dims.radius.r12) }
    val interactive = onClick != null && enabled

    var boxModifier = modifier
        .clip(shape)
        .background(resolved.container)
        .border(dims.stroke.st1, resolved.border, shape)
    if (!dense) boxModifier = boxModifier.heightIn(min = dims.sizing.chipHeight)
    if (interactive) boxModifier = boxModifier.debouncedClickable(onClick = onClick!!)
    boxModifier = if (dense) {
        boxModifier.padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s4)
    } else {
        boxModifier.padding(horizontal = if (interactive) dims.spacing.s16 else dims.spacing.s8)
    }

    val fontSize = if (dense) dims.fontSize.sm else dims.fontSize.md

    Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                PMIcon(
                    imageVector = leadingIcon,
                    tint = resolved.content,
                    size = dims.sizing.iconSm
                )
            }
            PMText(
                text = label,
                style = if (dense) PMTextStyle.Caption else PMTextStyle.Body,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                color = resolved.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (count != null) {
                PMText(
                    text = count.toString(),
                    fontSize = fontSize,
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
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    var selected by remember { mutableStateOf(setOf(0, 2)) }
    val tags = listOf("Nazik", "Yol verdi", "Saygılı", "Sabırlı")

    FlowRow(
        modifier = Modifier.padding(dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
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
        PMChip(label = "Kesik", style = PMChipStyle.Soft, dense = true, accentColor = colors.star, leadingIcon = Icons.Filled.Star)

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
