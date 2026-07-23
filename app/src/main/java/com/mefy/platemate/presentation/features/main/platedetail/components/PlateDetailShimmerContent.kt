package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.ShimmerBlock
import com.mefy.platemate.presentation.components.rememberShimmer
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PlateDetailShimmerContent(
    modifier: Modifier = Modifier
) {
    val sizing = PMTheme.sizing
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val radius = PMTheme.radius
    val shape = PMTheme.shapes
    val shimmer = rememberShimmer()

    Column(
        modifier = modifier
            .background(colors.surface)
            .padding(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s24)
    ) {
        // Topbar iskeleti (geri + başlık + bookmark)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(sizing.iconLg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(sizing.iconMd),
                shape = CircleShape
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.width(140.dp).height(22.dp),
                shape = RoundedCornerShape(radius.r8)
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(sizing.iconMd),
                shape = CircleShape
            )
        }

        // Plate header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(
                    width = sizing.plateBadgeMd * 3f,
                    height = sizing.plateBadgeMd
                ),
                shape = shape.medium
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(0.62f).height(20.dp),
                    shape = shape.medium
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(0.36f).height(13.dp),
                    shape = shape.medium
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(width = 48.dp, height = 28.dp),
                    shape = shape.medium
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(width = 66.dp, height = 12.dp),
                    shape = shape.medium
                )
            }
        }

        // Rating breakdown bars
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s12)) {
            repeat(5) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(width = 10.dp, height = 12.dp),
                        shape = shape.medium
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.weight(1f).height(7.dp),
                        shape = shape.full
                    )
                }
            }
        }

        // Tags
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(width = 74.dp, height = 11.dp),
                shape = shape.medium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                listOf(92.dp, 78.dp, 88.dp, 70.dp).forEach { chipWidth ->
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .width(chipWidth)
                            .height(sizing.chipHeight),
                        shape = shape.full
                    )
                }
            }
        }

        // Reviews
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s16)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(width = 128.dp, height = 11.dp),
                shape = shape.medium
            )
            repeat(3) {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier.size(sizing.avatarMd),
                            shape = shape.full
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(spacing.s8)
                        ) {
                            ShimmerBlock(
                                shimmer = shimmer,
                                modifier = Modifier.fillMaxWidth(0.42f).height(13.dp),
                                shape = shape.medium
                            )
                            ShimmerBlock(
                                shimmer = shimmer,
                                modifier = Modifier.size(width = 58.dp, height = 11.dp),
                                shape = shape.medium
                            )
                        }
                    }
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth().height(11.dp),
                        shape = shape.medium
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(0.72f).height(11.dp),
                        shape = shape.medium
                    )
                }
            }
        }
    }
}

@Preview(name = "PlateDetailShimmer Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateDetailShimmerLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateDetailShimmerContent(modifier = Modifier.fillMaxSize())
    }
}

@Preview(name = "PlateDetailShimmer Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateDetailShimmerDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateDetailShimmerContent(modifier = Modifier.fillMaxSize())
    }
}
