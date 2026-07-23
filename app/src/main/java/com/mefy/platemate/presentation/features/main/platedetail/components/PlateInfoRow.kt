package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.common.formatter.NumberFormatter
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateInfoRow(
    plateCode: String,
    cityName: String,
    ratingAverage: Double,
    reviewCount: Long
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMPlateBadge(
            plate = plateCode,
            size = sizing.plateBadgeMd
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            PMText(
                text = cityName,
                color = colors.textTertiary
            )
        }

        if (reviewCount > 0) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(spacing.s4)
            ) {
                PMText(
                    text = NumberFormatter.formatRating(ratingAverage),
                    fontSize = fontSize.lg,
                    color = colors.textPrimary
                )
                PMRatingStars(
                    rating = ratingAverage.toInt(),
                )
                PMText(
                    text = stringResource(R.string.platedetail_review_count, reviewCount),
                    color = colors.textLabel
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(spacing.s4)
            ) {
                PMText(
                    text = "—",
                    fontSize = fontSize.lg,
                    color = colors.textLabel
                )
                PMRatingStars(
                    rating = 0,
                )
                PMText(
                    text = stringResource(R.string.platedetail_no_rating),
                    color = colors.textLabel
                )
            }
        }
    }
}

@Preview(name = "PlateInfoRow Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateInfoRowLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateInfoRowPreviewContent()
    }
}

@Preview(name = "PlateInfoRow Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun PlateInfoRowDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateInfoRowPreviewContent()
    }
}

@Composable
private fun PlateInfoRowPreviewContent() {
    val spacing = PMTheme.spacing
    Column(
        modifier = Modifier.padding(spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        // Puanlı
        PlateInfoRow(
            plateCode = "34 EK 0682",
            cityName = "İstanbul",
            ratingAverage = 4.6,
            reviewCount = 127
        )
        // Puansız
        PlateInfoRow(
            plateCode = "06 ABC 123",
            cityName = "Ankara",
            ratingAverage = 0.0,
            reviewCount = 0
        )
    }
}