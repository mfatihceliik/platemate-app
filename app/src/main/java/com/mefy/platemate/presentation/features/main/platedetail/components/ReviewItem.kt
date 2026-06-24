package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.platedetail.PlateReviewUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ReviewItem(
    review: PlateReviewUiModel
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dims.spacing.s4),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.avatarSmall)
                .clip(CircleShape)
                .background(colors.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = review.initials,
                fontSize = dims.fontSize.md,
                fontWeight = FontWeight.Bold,
                color = colors.onPrimaryContainer
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = review.displayName ?: review.username,
                    fontSize = dims.fontSize.md,
                    color = colors.textPrimary
                )
                PMText(
                    text = review.timeAgo,
                    color = colors.textLabel
                )
            }

            PMRatingStars(
                rating = review.rating,
                starSize = dims.sizing.iconSm
            )

            if (!review.comment.isNullOrBlank()) {
                PMText(
                    text = review.comment,
                    color = colors.textSecondary
                )
            }

            if (review.reportTags.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                ) {
                    review.reportTags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dims.radius.rFull))
                                .background(colors.surfaceVariant)
                                .padding(
                                    horizontal = dims.spacing.s8,
                                    vertical = dims.spacing.s4
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            PMText(
                                text = tag,
                                fontSize = dims.fontSize.xs,
                                color = colors.textSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "ReviewItem Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReviewItemLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReviewItemPreviewContent()
    }
}

@Preview(name = "ReviewItem Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ReviewItemDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReviewItemPreviewContent()
    }
}

@Composable
private fun ReviewItemPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = Modifier.padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        ReviewItem(
            review = PlateReviewUiModel(
                id = 1,
                username = "ahmetk",
                displayName = "Ahmet K.",
                initials = "AK",
                profilePhotoUrl = null,
                rating = 5,
                timeAgo = "2 gün önce",
                comment = "Çok nazik bir sürücü, teşekkür etti.",
                reportTags = listOf("POLITE", "GAVE_WAY")
            )
        )
        ReviewItem(
            review = PlateReviewUiModel(
                id = 2,
                username = "zeynept",
                displayName = null,
                initials = "ZT",
                profilePhotoUrl = null,
                rating = 4,
                timeAgo = "3 gün önce",
                comment = null,
                reportTags = emptyList()
            )
        )
    }
}