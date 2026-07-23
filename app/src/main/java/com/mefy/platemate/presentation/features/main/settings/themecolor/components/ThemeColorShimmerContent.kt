package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.ShimmerBlock
import com.mefy.platemate.presentation.components.rememberShimmer
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

/** Loading skeleton for the Theme Color screen: preview card, accent grid, appearance selector. */
@Composable
internal fun ThemeColorShimmerContent(
    modifier: Modifier = Modifier,
    gridSize: Int = 4,
    swatchCount: Int = 8
) {
    val sizing = PMTheme.sizing
    val shapes = PMTheme.shapes
    val spacing = PMTheme.spacing
    val shimmer = rememberShimmer()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        // "Preview" label + card
        ShimmerBlock(shimmer, Modifier.fillMaxWidth(0.3f).height(spacing.s16), shapes.medium)
        ShimmerBlock(shimmer, Modifier.fillMaxWidth().height(sizing.gridHeight), shapes.medium)

        // "Accent color" label + swatch grid
        ShimmerBlock(shimmer, Modifier.fillMaxWidth(0.4f).height(spacing.s16), shapes.medium)
        val cols = gridSize.coerceAtLeast(1)
        val rows = (swatchCount + cols - 1) / cols
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s12)) {
            repeat(rows) { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(spacing.s12)) {
                    repeat(cols) { col ->
                        if (row * cols + col < swatchCount) {
                            ShimmerBlock(shimmer, Modifier.size(sizing.plateBadgeMd), CircleShape)
                        }
                    }
                }
            }
        }

        // "Appearance" label + three tabs
        ShimmerBlock(shimmer, Modifier.fillMaxWidth(0.35f).height(spacing.s16), shapes.medium)
        Row(horizontalArrangement = Arrangement.spacedBy(spacing.s12)) {
            repeat(3) {
                ShimmerBlock(shimmer, Modifier.weight(1f).height(sizing.buttonMinHeight), shapes.medium)
            }
        }
    }
}

@Preview(name = "ThemeColorShimmer Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ThemeColorShimmerLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ThemeColorShimmerContent(modifier = Modifier.fillMaxWidth())
    }
}

@Preview(name = "ThemeColorShimmer Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ThemeColorShimmerDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ThemeColorShimmerContent(modifier = Modifier.fillMaxWidth())
    }
}
