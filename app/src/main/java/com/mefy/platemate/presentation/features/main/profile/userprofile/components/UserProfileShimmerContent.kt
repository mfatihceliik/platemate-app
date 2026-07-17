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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
internal fun UserProfileShimmerContent(modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shimmer = rememberShimmer()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.surface)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.size(dims.sizing.avatarXl),
                shape = CircleShape,
            )
            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.width(140.dp).height(dims.spacing.s16),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.width(100.dp).height(dims.spacing.s12),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
            }
        }

        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth().height(dims.spacing.s12),
            shape = RoundedCornerShape(dims.radius.r8),
        )
        ShimmerBlock(
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(0.6f).height(dims.spacing.s12),
            shape = RoundedCornerShape(dims.radius.r8),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            repeat(3) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(dims.radius.r12),
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.weight(1f).height(dims.sizing.buttonMinHeight),
                shape = RoundedCornerShape(dims.radius.r12),
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.weight(1f).height(dims.sizing.buttonMinHeight),
                shape = RoundedCornerShape(dims.radius.r12),
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
    val dims = MaterialTheme.pmDimensions

    PMCard(
        modifier = Modifier.fillMaxWidth(),
        padding = PaddingValues(dims.spacing.s12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s10)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(dims.sizing.plateBadgeSm),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.weight(1f).height(dims.spacing.s12),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.width(32.dp).height(dims.spacing.s16),
                    shape = RoundedCornerShape(dims.radius.r8),
                )
            }
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth().height(dims.spacing.s12),
                shape = RoundedCornerShape(dims.radius.r8),
            )
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(0.7f).height(dims.spacing.s12),
                shape = RoundedCornerShape(dims.radius.r8),
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
