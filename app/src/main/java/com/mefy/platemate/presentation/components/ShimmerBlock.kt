package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.theme.pmColors
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
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(
                color = MaterialTheme.pmColors.skeleton.copy(alpha = 0.75f),
                shape = shape,
            )
    )
}

@Composable
internal fun rememberShimmer(): Shimmer {
    val colors = MaterialTheme.pmColors

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
