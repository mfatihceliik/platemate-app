package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.ShimmerBlock
import com.mefy.platemate.presentation.components.rememberShimmer
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

/** Loading skeleton for the Theme Color screen: preview card, accent grid, appearance selector. */
@Composable
internal fun ThemeColorShimmerContent(
    modifier: Modifier = Modifier,
    gridSize: Int = 4,
    swatchCount: Int = 8
) {
    val dims = MaterialTheme.pmDimensions
    val shimmer = rememberShimmer()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        // "Preview" label + card
        ShimmerBlock(shimmer, Modifier.fillMaxWidth(0.3f).height(dims.spacing.s16), RoundedCornerShape(dims.radius.r8))
        ShimmerBlock(shimmer, Modifier.fillMaxWidth().height(dims.sizing.gridHeight), RoundedCornerShape(dims.radius.r16))

        // "Accent color" label + swatch grid
        ShimmerBlock(shimmer, Modifier.fillMaxWidth(0.4f).height(dims.spacing.s16), RoundedCornerShape(dims.radius.r8))
        val cols = gridSize.coerceAtLeast(1)
        val rows = (swatchCount + cols - 1) / cols
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)) {
            repeat(rows) { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)) {
                    repeat(cols) { col ->
                        if (row * cols + col < swatchCount) {
                            ShimmerBlock(shimmer, Modifier.size(dims.sizing.plateBadgeMd), CircleShape)
                        }
                    }
                }
            }
        }

        // "Appearance" label + three tabs
        ShimmerBlock(shimmer, Modifier.fillMaxWidth(0.35f).height(dims.spacing.s16), RoundedCornerShape(dims.radius.r8))
        Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)) {
            repeat(3) {
                ShimmerBlock(shimmer, Modifier.weight(1f).height(dims.sizing.buttonMinHeight), RoundedCornerShape(dims.radius.r12))
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
