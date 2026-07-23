package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMPlateBadge(
    modifier: Modifier = Modifier,
    plate: String,
    size: Dp = PMTheme.sizing.plateBadgeMd
) {
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize
    val stroke = PMTheme.stroke
    val colors = PMTheme.colors
    val shape = PMTheme.shapes.medium
    val bandWidth = size * 0.45f

    Row(
        modifier = modifier
            .height(size)
            .clip(shape)
            .background(colors.surface)
            .border(stroke.st1, colors.surfaceVariant, shape)

    ) {
        Box(
            modifier = Modifier
                .width(bandWidth)
                .fillMaxHeight()
                .background(colors.plateBadge),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = "TR",
                color = colors.textWhite,
                fontSize = fontSize.xs,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(top = spacing.s8)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = spacing.s8),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = plate,
                color = colors.textPrimary,
                fontSize = fontSize.lg,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
            )
        }
    }
}

@Preview(name = "PMPlateBadge Sizes", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPlateBadgeSizesPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {

        val spacing = PMTheme.spacing
        val sizing = PMTheme.sizing
        val fontSize = PMTheme.fontSize
        val colors = PMTheme.colors

        Column(
            modifier = Modifier.padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(plate = "34", size = sizing.plateBadgeSm)
                PMPlateBadge(plate = "34", size = sizing.plateBadgeMd)
            }
            PMPlateBadge(plate = "34 EK 0682", size = sizing.plateBadgeLg)
            PMPlateBadge(plate = "34 EK 0682", size = sizing.plateBadgeXl)
        }
    }
}

@Preview(name = "PMPlateBadge Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMPlateBadgeDarkPreview() {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        Column(
            modifier = Modifier.padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            PMPlateBadge(plate = "06", size = sizing.plateBadgeSm)
            PMPlateBadge(plate = "06 ABC 123", size = sizing.plateBadgeMd)
        }
    }
}
