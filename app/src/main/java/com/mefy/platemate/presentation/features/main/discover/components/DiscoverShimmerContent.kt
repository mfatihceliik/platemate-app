package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.ShimmerBlock
import com.mefy.platemate.presentation.components.rememberShimmer
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun DiscoverShimmerContent(modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val shimmer = rememberShimmer()

    LazyColumn(
        modifier = modifier.background(MaterialTheme.pmColors.background),
        contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .height(dims.spacing.s32),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(dims.spacing.s16),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                repeat(4) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .height(dims.sizing.chipHeight)
                            .weight(1f),
                        shape = RoundedCornerShape(dims.radius.rFull),
                    )
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r8),
            )
        }

        items(3, contentType = { "shimmer_row" }) {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(dims.spacing.s12),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(dims.sizing.rankBadgeSize),
                        shape = RoundedCornerShape(dims.radius.r8),
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(width = 42.dp, height = 28.dp),
                        shape = RoundedCornerShape(dims.radius.r8),
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    ) {
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(dims.spacing.s16),
                            shape = RoundedCornerShape(dims.radius.r8),
                        )
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(dims.spacing.s12),
                            shape = RoundedCornerShape(dims.radius.r8),
                        )
                    }
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r8),
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            ) {
                repeat(2) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .weight(1f)
                            .height(104.dp),
                        shape = RoundedCornerShape(dims.radius.r16),
                    )
                }
            }
        }
    }
}




@Preview(name = "DiscoverShimmerContent Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverShimmerContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverShimmerContent(modifier = Modifier.fillMaxSize())
    }
}

@Preview(name = "DiscoverShimmerContent Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun DiscoverShimmerContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        DiscoverShimmerContent(modifier = Modifier.fillMaxSize())
    }
}
