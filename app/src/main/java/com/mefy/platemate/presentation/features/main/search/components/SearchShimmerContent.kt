package com.mefy.platemate.presentation.features.main.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.ShimmerBlock
import com.mefy.platemate.presentation.components.rememberShimmer
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.valentinilk.shimmer.Shimmer

@Composable
internal fun SearchShimmerContent(
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val shapes = PMTheme.shapes
    val shimmer = rememberShimmer()

    LazyColumn(
        modifier = modifier.background(colors.background),
        contentPadding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s16),
    ) {
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sizing.searchBarHeight),
                shape = shapes.medium,
            )
        }

        // "Son aramalar" başlığı + chip satırı
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(spacing.s16),
                shape = shapes.medium,
            )
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                verticalArrangement = Arrangement.spacedBy(spacing.s8),
            ) {
                listOf(96.dp, 80.dp, 110.dp, 72.dp, 92.dp).forEach { chipWidth ->
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .width(chipWidth)
                            .height(sizing.chipHeight),
                        shape = shapes.medium,
                    )
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(spacing.s16),
                shape = shapes.medium,
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
                repeat(2) {
                    SavedCardSkeleton(shimmer = shimmer)
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(spacing.s16),
                shape = shapes.medium,
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
                repeat(2) {
                    SavedCardSkeleton(shimmer = shimmer)
                }
            }
        }

        item {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s12),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(sizing.iconLg),
                        shape = shapes.medium,
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(spacing.s12),
                        shape = shapes.medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedCardSkeleton(
    shimmer: Shimmer
) {
    val sizing = PMTheme.sizing
    val shapes = PMTheme.shapes
    val spacing = PMTheme.spacing

    PMCard(
        modifier = Modifier.width(sizing.savedPlateCardWidth),
        padding = PaddingValues(spacing.s12),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing.s8),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(
                        width = sizing.plateBadgeSm * 3f,
                        height = sizing.plateBadgeSm
                    ),
                    shape = shapes.medium,
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(sizing.iconXl),
                    shape = shapes.medium,
                )
            }
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(spacing.s12),
                shape = shapes.medium,
            )
        }
    }
}

@Preview(name = "SearchShimmerContent Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SearchShimmerContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SearchShimmerContent(modifier = Modifier.fillMaxSize())
    }
}

@Preview(name = "SearchShimmerContent Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SearchShimmerContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SearchShimmerContent(modifier = Modifier.fillMaxSize())
    }
}
