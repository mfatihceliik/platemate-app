package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import com.mefy.platemate.presentation.components.ShimmerBlock
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer

@Composable
internal fun ProfileShimmerContent(
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val shimmerTheme = remember(colors) {
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

    LazyColumn(
        modifier = modifier.background(colors.background),
        contentPadding = PaddingValues(bottom = dims.spacing.s24),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s16)
                    .height(260.dp),
                shape = RoundedCornerShape(bottomStart = dims.radius.r16, bottomEnd = dims.radius.r16)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                repeat(3) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        shape = RoundedCornerShape(dims.radius.r16)
                    )
                }
            }
        }

        items(3) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r16)
            )
        }
    }
}

@Preview(name = "ProfileShimmer Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ProfileShimmerContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileShimmerContent(modifier = Modifier.fillMaxSize())
    }
}

@Preview(name = "ProfileShimmer Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ProfileShimmerContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileShimmerContent(modifier = Modifier.fillMaxSize())
    }
}