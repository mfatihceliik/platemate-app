package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMCardVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.components.util.reviewStatusStyle
import com.mefy.platemate.presentation.features.uimodel.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.uimodel.ProfileReviewStatusUi
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PlateReviewActivityCard(
    item: PlateReviewNotificationItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    // Stable, id-aware callback so the card can skip recomposition while data is unchanged.
    val cardClick = remember(item.normalizedPlateCode, onClick) {
        { onClick(item.normalizedPlateCode) }
    }

    PMCard(
        modifier = modifier.fillMaxWidth(),
        onClick = cardClick,
        variant = PMCardVariant.Large,
        padding = PaddingValues(dims.spacing.s0)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(dims.sizing.stripeWidthLarge)
                    .fillMaxHeight()
                    .background(colors.primary)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = dims.spacing.s16,
                        vertical = dims.spacing.s12
                    ),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                // Üst sıra: plaka + durum rozeti
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PMPlateBadge(
                            cityCode = item.plateCode.take(2),
                            size = PlateBadgeSize.Review
                        )
                        PMText(
                            text = item.plateCode,
                            fontSize = dims.fontSize.md,
                            style = PMTextStyle.Title,
                            color = colors.textPrimary
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val style = reviewStatusStyle(item.reviewStatus, colors)
                            PMChip(
                                label = stringResource(style.label),
                                containerColor = style.background,
                                accentColor = style.foreground
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PMIcon(
                                imageVector = Icons.Filled.Star,
                                size = dims.sizing.iconSm
                            )
                            PMText(
                                text = NumberFormatter.formatRating(item.ratingAverage),
                                fontSize = dims.fontSize.md
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PMIcon(
                                imageVector = Icons.Filled.ChatBubble,
                                size = dims.sizing.iconSm
                            )
                            PMText(
                                text = item.commentCount.toString(),
                                fontSize = dims.fontSize.md
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = item.createdAtText,
                        fontSize = dims.fontSize.sm,
                        color = colors.textLabel
                    )
                    PMIcon(
                        imageVector = Icons.Filled.ChevronRight,
                        size = dims.sizing.iconSm
                    )
                }
            }
        }
    }
}

@Preview(name = "PlateReviewCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateReviewActivityCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateReviewActivityCardPreviewContent()
    }
}

@Preview(name = "PlateReviewCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateReviewActivityCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateReviewActivityCardPreviewContent()
    }
}

@Composable
private fun PlateReviewActivityCardPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = Modifier.padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        PlateReviewActivityCard(
            item = PlateReviewNotificationItem(
                id = "review_1",
                normalizedPlateCode = "34AB1234",
                plateCode = "34 AB 1234",
                ratingAverage = 4.0,
                commentCount = 1,
                reviewStatus = ProfileReviewStatusUi.APPROVED,
                createdAtText = "2026-05-27",
                sortKey = "2026-05-27T10:00:00Z"
            ),
            onClick = { }
        )
        PlateReviewActivityCard(
            item = PlateReviewNotificationItem(
                id = "review_2",
                normalizedPlateCode = "06XYZ06",
                plateCode = "06 XYZ 06",
                ratingAverage = 2.5,
                commentCount = 12,
                reviewStatus = ProfileReviewStatusUi.PENDING_REVIEW,
                createdAtText = "2026-05-26",
                sortKey = "2026-05-26T10:00:00Z"
            ),
            onClick = { }
        )
    }
}
