package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun EmptyPlateState() {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dims.spacing.s16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.plateBadgeLg)
                .clip(CircleShape)
                .background(colors.searchFieldBg),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.Outlined.RateReview,
                tint = colors.textLabel,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMText(
                text = stringResource(R.string.platedetail_empty_reviews_title),
                fontSize = dims.fontSize.md,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            )
            PMText(
                text = stringResource(R.string.platedetail_empty_reviews_subtitle),
                fontSize = dims.fontSize.sm,
                color = colors.textTertiary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(dims.spacing.s8))

        EmptyInfoRow(
            icon = Icons.Filled.Star,
            text = stringResource(R.string.platedetail_rate_driving)
        )
        EmptyInfoRow(
            icon = Icons.Outlined.Sell,
            text = stringResource(R.string.platedetail_tag_driver)
        )
        EmptyInfoRow(
            icon = Icons.Outlined.RateReview,
            text = stringResource(R.string.platedetail_share_comment)
        )
    }
}

@Preview(name = "EmptyPlateState Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun EmptyPlateStateLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        EmptyPlateState()
    }
}

@Preview(name = "EmptyPlateState Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun EmptyPlateStateDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        EmptyPlateState()
    }
}