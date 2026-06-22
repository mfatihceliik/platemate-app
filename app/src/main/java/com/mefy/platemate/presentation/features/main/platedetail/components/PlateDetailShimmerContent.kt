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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.ShimmerBlock
import com.mefy.platemate.presentation.components.rememberShimmer
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PlateDetailShimmerContent(modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val shimmer = rememberShimmer()

    Column(
        modifier = modifier
            .background(MaterialTheme.pmColors.surface)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s24)
    ) {
        // Topbar iskeleti (geri + başlık + bookmark)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dims.sizing.iconHuge),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(dims.sizing.iconLg),
                shape = CircleShape
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.width(140.dp).height(22.dp),
                shape = RoundedCornerShape(dims.radius.r8)
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(dims.sizing.iconLg),
                shape = CircleShape
            )
        }

        // Plate header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(dims.sizing.plateBadgeLarge),
                shape = RoundedCornerShape(dims.radius.r16)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(0.62f).height(20.dp),
                    shape = RoundedCornerShape(dims.radius.r8)
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(0.36f).height(13.dp),
                    shape = RoundedCornerShape(dims.radius.r8)
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(width = 48.dp, height = 28.dp),
                    shape = RoundedCornerShape(dims.radius.r8)
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(width = 66.dp, height = 12.dp),
                    shape = RoundedCornerShape(dims.radius.r8)
                )
            }
        }

        // Rating breakdown bars
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)) {
            repeat(5) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(width = 10.dp, height = 12.dp),
                        shape = RoundedCornerShape(dims.radius.r8)
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.weight(1f).height(7.dp),
                        shape = RoundedCornerShape(dims.radius.rFull)
                    )
                }
            }
        }

        // Tags
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(width = 74.dp, height = 11.dp),
                shape = RoundedCornerShape(dims.radius.r8)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                listOf(92.dp, 78.dp, 88.dp, 70.dp).forEach { chipWidth ->
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .width(chipWidth)
                            .height(dims.sizing.chipHeight),
                        shape = RoundedCornerShape(dims.radius.rFull)
                    )
                }
            }
        }

        // Reviews
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(width = 128.dp, height = 11.dp),
                shape = RoundedCornerShape(dims.radius.r8)
            )
            repeat(3) {
                Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier.size(dims.sizing.avatarSmall),
                            shape = RoundedCornerShape(percent = 50)
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                        ) {
                            ShimmerBlock(
                                shimmer = shimmer,
                                modifier = Modifier.fillMaxWidth(0.42f).height(13.dp),
                                shape = RoundedCornerShape(dims.radius.r8)
                            )
                            ShimmerBlock(
                                shimmer = shimmer,
                                modifier = Modifier.size(width = 58.dp, height = 11.dp),
                                shape = RoundedCornerShape(dims.radius.r8)
                            )
                        }
                    }
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth().height(11.dp),
                        shape = RoundedCornerShape(dims.radius.r8)
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(0.72f).height(11.dp),
                        shape = RoundedCornerShape(dims.radius.r8)
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
