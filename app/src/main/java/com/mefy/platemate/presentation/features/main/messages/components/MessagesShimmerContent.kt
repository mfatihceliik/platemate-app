package com.mefy.platemate.presentation.features.main.messages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
internal fun MessagesShimmerContent(
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.pmColors

    val shimmerTheme = remember(colorScheme) {
        defaultShimmerTheme.copy(
            shaderColors = listOf(
                colors.skeleton.copy(alpha = 0.55f),
                colors.surface.copy(alpha = 0.95f),
                colors.skeletonSecondary.copy(alpha = 0.45f)
            ),
            shaderColorStops = listOf(0f, 0.5f, 1f)
        )
    }
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View, theme = shimmerTheme)

    Column(
        modifier = modifier.padding(horizontal = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        repeat(6) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dims.sizing.avatarMd)
                        .shimmer(shimmer)
                        .background(colors.skeleton, CircleShape)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(dims.spacing.s16)
                            .shimmer(shimmer)
                            .background(colors.skeleton, RoundedCornerShape(dims.radius.r8))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(dims.spacing.s16)
                            .shimmer(shimmer)
                            .background(colors.skeleton.copy(alpha = 0.5f), RoundedCornerShape(dims.radius.r8))
                    )
                }
                Box(
                    modifier = Modifier
                        .size(width = 35.dp, height = dims.spacing.s12)
                        .shimmer(shimmer)
                        .background(colors.skeleton, RoundedCornerShape(dims.radius.r8))
                )
            }
        }
    }
}