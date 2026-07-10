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
import com.valentinilk.shimmer.Shimmer

@Composable
internal fun SearchShimmerContent(modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val shimmer = rememberShimmer()

    LazyColumn(
        modifier = modifier.background(MaterialTheme.pmColors.background),
        contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16),
    ) {
        // Arama çubuğu
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dims.sizing.searchBarHeight),
                shape = RoundedCornerShape(dims.radius.r12),
            )
        }

        // "Son aramalar" başlığı + chip satırı
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r8),
            )
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            ) {
                listOf(96.dp, 80.dp, 110.dp, 72.dp, 92.dp).forEach { chipWidth ->
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .width(chipWidth)
                            .height(dims.sizing.chipHeight),
                        shape = RoundedCornerShape(dims.radius.rFull),
                    )
                }
            }
        }

        // "Kayıtlı" başlığı + yatay kart satırı
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r8),
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                repeat(2) {
                    SavedCardSkeleton(shimmer = shimmer)
                }
            }
        }

        // "Alarmlar" başlığı + yatay kart satırı
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r8),
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                repeat(2) {
                    SavedCardSkeleton(shimmer = shimmer)
                }
            }
        }

        item {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s12),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(dims.sizing.iconLg),
                        shape = RoundedCornerShape(dims.radius.r8),
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(dims.spacing.s12),
                        shape = RoundedCornerShape(dims.radius.r8),
                    )
                }
            }
        }
    }
}

/** SavedPlateCompactCard ayak izini taklit eden iskelet kartı. */
@Composable
private fun SavedCardSkeleton(shimmer: Shimmer) {
    val dims = MaterialTheme.pmDimensions

    PMCard(
        modifier = Modifier.width(dims.sizing.savedPlateCardWidth),
        padding = PaddingValues(dims.spacing.s12),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(
                        width = dims.sizing.plateBadgeSm * 3f,
                        height = dims.sizing.plateBadgeSm
                    ),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(dims.sizing.iconXl),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
            }
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(dims.spacing.s12),
                shape = RoundedCornerShape(dims.radius.r8),
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
