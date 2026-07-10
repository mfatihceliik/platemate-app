package com.mefy.platemate.presentation.features.main.platedetail.review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PlateInfoCard(state: ReviewUiState) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dims.radius.r16))
            .background(colors.surfaceSecondary)
            .border(dims.stroke.st1, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(dims.radius.r16))
            .padding(dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMPlateBadge(
            plate = state.plateCode,
            size = dims.sizing.plateBadgeMd
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = if (state.cityName.isNotBlank()) {
                    stringResource(R.string.review_city_review_count, state.cityName, state.reviewCount)
                } else {
                    stringResource(R.string.review_count_format, state.reviewCount)
                },
                fontSize = dims.fontSize.sm,
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