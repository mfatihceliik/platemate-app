package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
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
internal fun UserProfileShimmerContent(
    modifier: Modifier = Modifier
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val shape = PMTheme.shapes
    val shimmer = rememberShimmer()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(sizing.avatarXl),
                shape = CircleShape,
            )
            Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.width(140.dp).height(spacing.s16),
                    shape = shape.medium,
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.width(100.dp).height(spacing.s12),
                    shape = shape.medium,
                )
            }
        }

        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth().height(spacing.s12),
            shape = shape.medium,
        )
        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(0.6f).height(spacing.s12),
            shape = shape.medium,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
            repeat(3) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = shape.medium,
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.weight(1f).height(sizing.buttonMinHeight),
                shape = shape.medium,
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.weight(1f).height(sizing.buttonMinHeight),
                shape = shape.medium,
            )
        }

        HorizontalDivider(color = colors.outlineVariant)

        repeat(2) {
            ReviewCardSkeleton(shimmer = shimmer)
        }
    }
}

@Composable
private fun ReviewCardSkeleton(shimmer: Shimmer) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val shape = PMTheme.shapes

    PMCard(
        modifier = Modifier.fillMaxWidth(),
        padding = PaddingValues(spacing.s12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s10)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.s10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(sizing.plateBadgeSm),
                    shape = shape.medium,
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.weight(1f).height(spacing.s12),
                    shape = shape.medium,
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.width(32.dp).height(spacing.s16),
                    shape = shape.medium,
                )
            }
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth().height(spacing.s12),
                shape = shape.medium,
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(0.7f).height(spacing.s12),
                shape = shape.medium,
            )
        }
    }
}

@Preview(name = "UserProfileShimmerContent Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileShimmerContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        UserProfileShimmerContent()
    }
}

@Preview(name = "UserProfileShimmerContent Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileShimmerContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        UserProfileShimmerContent()
    }
}
