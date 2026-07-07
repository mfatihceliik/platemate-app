package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.features.uimodel.UserProfileReviewUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun UserProfileReviewCard(
    review: UserProfileReviewUiModel,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val cardShape = MaterialTheme.shapes.large

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = dims.spacing.s4, shape = cardShape)
            .background(colors.surface, cardShape)
            .border(dims.stroke.st1, colors.outlineVariant, cardShape)
            .padding(dims.spacing.s12),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PMPlateBadge(
                cityCode = review.plateCode,
                size = PlateBadgeSize.Small
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = review.plateNumber,
                    style = PMTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                PMText(
                    text = "${review.city} · ${review.date}",
                    style = PMTextStyle.Note,
                    color = colors.textLabel
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMIcon(
                    imageVector = Icons.Filled.Star,
                    tint = colors.star,
                    size = dims.sizing.iconSm,
                )
                PMText(
                    text = if (review.rating % 1 == 0f) review.rating.toInt().toString()
                           else NumberFormatter.formatRating(review.rating.toDouble()),
                    style = PMTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }
        }

        if (review.tags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                review.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .height(dims.spacing.s24)
                            .background(colors.primaryContainer, MaterialTheme.shapes.extraLarge)
                            .padding(horizontal = dims.spacing.s10),
                        contentAlignment = Alignment.Center
                    ) {
                        PMText(
                            text = tag,
                            style = PMTextStyle.Note,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onPrimaryContainer
                        )
                    }
                }
            }
        }

        PMText(
            text = review.comment,
            style = PMTextStyle.Note,
            color = colors.textSecondary
        )
    }
}

private val previewReview = UserProfileReviewUiModel(
    id = 1L,
    plateCode = "34",
    plateNumber = "34 EK 0682",
    city = "İstanbul",
    date = "12 Haz 2025",
    rating = 4.5f,
    tags = listOf("Nazik", "Yol verdi", "Saygılı"),
    comment = "Çok nazik bir sürücü, yol verdi ve güvenli mesafe bıraktı."
)

@Preview(name = "UserProfileReviewCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileReviewCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        UserProfileReviewCard(review = previewReview, modifier = Modifier.padding(dims.spacing.s16))
    }
}

@Preview(name = "UserProfileReviewCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileReviewCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        UserProfileReviewCard(review = previewReview, modifier = Modifier.padding(dims.spacing.s16))
    }
}
