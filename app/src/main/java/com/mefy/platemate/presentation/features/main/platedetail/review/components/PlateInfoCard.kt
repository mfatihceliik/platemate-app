package com.mefy.platemate.presentation.features.main.platedetail.review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewUiState
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateInfoCard(state: ReviewUiState) {
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize
    val shape = PMTheme.shapes

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape.medium)
            .background(colors.surfaceSecondary)
            .border(stroke.st1, colors.outlineVariant, shape.medium)
            .padding(spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMPlateBadge(
            plate = state.plateCode,
            size = sizing.plateBadgeMd
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            PMText(
                text = if (state.cityName.isNotBlank()) {
                    stringResource(R.string.review_city_review_count, state.cityName, state.reviewCount)
                } else {
                    stringResource(R.string.review_count_format, state.reviewCount)
                },
                fontSize = fontSize.sm,
                color = colors.textTertiary
            )
        }
    }
}

private fun previewState() = ReviewUiState(
    plateCode = "34 EK 0682",
    cityCode = "34",
    cityName = "İstanbul",
    reviewCount = 127,
    isLoading = false
)

@Preview(name = "PlateInfoCard Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateInfoCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateInfoCard(state = previewState())
    }
}

@Preview(name = "PlateInfoCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateInfoCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateInfoCard(state = previewState())
    }
}