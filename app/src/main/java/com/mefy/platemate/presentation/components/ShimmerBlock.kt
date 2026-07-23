package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
internal fun ShimmerBlock(
    shimmer: Shimmer,
    modifier: Modifier,
    shape: RoundedCornerShape,
) {
    val colors = PMTheme.colors
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(
                color = colors.skeleton.copy(alpha = 0.75f),
                shape = shape,
            )
    )
}

@Composable
internal fun rememberShimmer(): Shimmer {
    val colors = PMTheme.colors

    val theme = remember(colors) {
        defaultShimmerTheme.copy(
            shaderColors = listOf(
                colors.skeleton.copy(alpha = 0.55f),
                colors.surface.copy(alpha = 0.95f),
                colors.skeletonSecondary.copy(alpha = 0.45f),
            ),
            shaderColorStops = listOf(0f, 0.5f, 1f),
        )
    }
    return rememberShimmer(shimmerBounds = ShimmerBounds.View, theme = theme)
}

@Preview(name = "ShimmerBlock Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ShimmerBlockLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ShimmerBlockPreviewContent()
    }
}

@Preview(name = "ShimmerBlock Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ShimmerBlockDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ShimmerBlockPreviewContent()
    }
}

@Composable
private fun ShimmerBlockPreviewContent() {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val shape = PMTheme.shapes.medium
    val shimmer = rememberShimmer()

    Column(verticalArrangement = Arrangement.spacedBy(spacing.s12)) {
        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.width(160.dp).height(spacing.s16),
            shape = shape
        )
        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.size(sizing.avatarMd),
            shape = RoundedCornerShape(percent = 50)
        )
    }
}
