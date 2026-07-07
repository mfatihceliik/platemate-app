package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PlateInfoRow(
    plateCode: String,
    cityCode: String,
    cityName: String,
    ratingAverage: Double,
    reviewCount: Long
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMPlateBadge(
            cityCode = cityCode,
            size = PlateBadgeSize.Large
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = plateCode,
                fontSize = dims.fontSize.xl,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textPrimary
            )
            PMText(
                text = cityName,
                color = colors.textTertiary
            )
        }

        if (reviewCount > 0) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = NumberFormatter.formatRating(ratingAverage),
                    fontSize = dims.fontSize.lg,
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
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = "—",
                    fontSize = dims.fontSize.lg,
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
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = Modifier.padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        // Puanlı
        PlateInfoRow(
            plateCode = "34 EK 0682",
            cityCode = "34",
            cityName = "İstanbul",
            ratingAverage = 4.6,
            reviewCount = 127
        )
        // Puansız
        PlateInfoRow(
            plateCode = "06 ABC 123",
            cityCode = "06",
            cityName = "Ankara",
            ratingAverage = 0.0,
            reviewCount = 0
        )
    }
}