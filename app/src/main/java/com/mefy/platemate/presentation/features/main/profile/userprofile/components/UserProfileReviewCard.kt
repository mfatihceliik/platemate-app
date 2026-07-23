package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.common.formatter.NumberFormatter
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.features.uimodel.UserProfileReviewUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun UserProfileReviewCard(
    modifier: Modifier = Modifier,
    review: UserProfileReviewUiModel
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val shape = PMTheme.shapes
    val sizing = PMTheme.sizing
    val fontSize = PMTheme.fontSize
    val stroke = PMTheme.stroke

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = spacing.s4, shape = shape.medium)
            .background(colors.surface, shape.medium)
            .border(stroke.st1, colors.outlineVariant, shape.medium)
            .drawBehind {
                drawRect(
                    color = colors.primary,
                    size = Size(spacing.s8.toPx(), size.height)
                )
            }
            .padding(spacing.s12),
        verticalArrangement = Arrangement.spacedBy(spacing.s10)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s10),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PMPlateBadge(
                plate = review.plateNumber,
                size = sizing.plateBadgeSm
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.s4)
            ) {
                PMText(
                    text = "${review.city} · ${review.date}",
                    style = PMTextStyle.Note,
                    color = colors.textLabel
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.s4)
            ) {
                PMIcon(
                    imageVector = Icons.Filled.Star,
                    tint = colors.iconStar,
                    size = sizing.iconSm,
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
                horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                verticalArrangement = Arrangement.spacedBy(spacing.s4)
            ) {
                review.tags.forEach { tag ->
                    PMChip(
                        label = tag,
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            if (review.comment.isNotEmpty()) {
                PMText(
                    text = "“",
                    fontSize = fontSize.xxl,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary.copy(alpha = 0.4f),
                    modifier = Modifier.padding(end = spacing.s4)
                )
                PMText(
                    text = review.comment,
                    fontSize = fontSize.md,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f).padding(top = spacing.s4)
                )
            }
        }
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
        val spacing = PMTheme.spacing
        UserProfileReviewCard(review = previewReview, modifier = Modifier.padding(spacing.s16))
    }
}

@Preview(name = "UserProfileReviewCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileReviewCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val spacing = PMTheme.spacing
        UserProfileReviewCard(review = previewReview, modifier = Modifier.padding(spacing.s16))
    }
}
