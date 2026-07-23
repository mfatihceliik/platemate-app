package com.mefy.platemate.presentation.features.main.platedetail.review.components

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
internal fun ReviewShimmerContent(modifier: Modifier = Modifier) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes
    val shimmer = rememberShimmer()

    Column(
        modifier = modifier
            .background(colors.surface)
            .padding(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s24)
    ) {
        // Plate info card
        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth().height(74.dp),
            shape = shape.medium
        )

        // Overall rating
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s12)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(width = 84.dp, height = 11.dp),
                shape = shape.medium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.s8, Alignment.CenterHorizontally)
            ) {
                repeat(5) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(34.dp),
                        shape = shape.medium
                    )
                }
            }
        }

        // Tags
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s12)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(width = 64.dp, height = 11.dp),
                shape = shape.medium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                listOf(70.dp, 86.dp, 74.dp, 96.dp, 64.dp, 88.dp, 80.dp, 72.dp).forEach { chipWidth ->
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .width(chipWidth)
                            .height(34.dp),
                        shape = shape.medium
                    )
                }
            }
        }

        // Experience textarea
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s12)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(width = 72.dp, height = 11.dp),
                shape = shape.medium
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = shape.medium
            )
        }
    }
}

@Preview(name = "ReviewShimmer Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReviewShimmerLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReviewShimmerContent(modifier = Modifier.fillMaxSize())
    }
}

@Preview(name = "ReviewShimmer Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReviewShimmerDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReviewShimmerContent(modifier = Modifier.fillMaxSize())
    }
}
