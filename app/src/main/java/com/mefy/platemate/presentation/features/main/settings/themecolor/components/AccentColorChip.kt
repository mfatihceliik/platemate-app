package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.settings.themecolor.AccentColors
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun AccentColorChip(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shapes = PMTheme.shapes
    val stroke = PMTheme.stroke
    val sizing = PMTheme.sizing
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(sizing.iconXl)
            .background(colors.surface, shapes.medium)
            .border(
                width = if (isSelected) stroke.st2 else stroke.st1,
                color = if (isSelected) color else colors.cardBorder,
                shape = shapes.medium
            )
            .debouncedClickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(spacing.s32)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Preview(name = "AccentColorChip Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccentColorChipLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AccentColorChipPreviewContent()
    }
}

@Preview(name = "AccentColorChip Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun AccentColorChipDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AccentColorChipPreviewContent()
    }
}

@Composable
private fun AccentColorChipPreviewContent() {
    val spacing = PMTheme.spacing

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
        modifier = Modifier.padding(spacing.s16)
    ) {
        AccentColorChip(
            color = AccentColors[0],
            isSelected = true,
            onClick = {},
            modifier = Modifier.width(64.dp)
        )
        AccentColorChip(
            color = AccentColors[2],
            isSelected = false,
            onClick = {},
            modifier = Modifier.width(64.dp)
        )
        AccentColorChip(
            color = AccentColors[5],
            isSelected = false,
            onClick = {},
            modifier = Modifier.width(64.dp)
        )
    }
}
