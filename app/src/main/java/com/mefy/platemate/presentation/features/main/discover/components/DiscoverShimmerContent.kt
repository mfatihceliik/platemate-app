package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
internal fun DiscoverShimmerContent(
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer()
    val spacing = PMTheme.spacing

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        // Subtitle Shimmer
        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.width(150.dp).height(20.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        )

        // Hero Stats Shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            repeat(3) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.weight(1f).height(80.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
            }
        }

        // For You Section Shimmer
        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        )

        // Filters Shimmer
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            repeat(4) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.width(80.dp).height(32.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
            }
        }

        // Trending Header Shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.width(120.dp).height(24.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.width(40.dp).height(40.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
        }

        // Trending Cards Shimmer
        repeat(3) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
        }
    }
}

@Preview(name = "DiscoverShimmer Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DiscoverShimmerContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverShimmerContent(modifier = Modifier.fillMaxWidth())
    }
}

@Preview(name = "DiscoverShimmer Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun DiscoverShimmerContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        DiscoverShimmerContent(modifier = Modifier.fillMaxWidth())
    }
}
