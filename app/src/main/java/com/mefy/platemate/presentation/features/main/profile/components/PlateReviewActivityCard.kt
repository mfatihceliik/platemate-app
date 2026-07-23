package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.common.formatter.NumberFormatter
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMCardVariant
import com.mefy.platemate.presentation.components.util.reviewStatusStyle
import com.mefy.platemate.presentation.features.uimodel.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.uimodel.ProfileReviewStatusUi
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateReviewActivityCard(
    item: PlateReviewNotificationItem,
    onClick: (String, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize
    val shape = PMTheme.shapes
    // Stable, id-aware callback so the card can skip recomposition while data is unchanged.
    val cardClick = remember(item.normalizedPlateCode, item.reviewId, onClick) {
        { onClick(item.normalizedPlateCode, item.reviewId) }
    }

    PMCard(
        modifier = modifier
            .fillMaxWidth(),
        onClick = cardClick,
        variant = PMCardVariant.Large,
        padding = PaddingValues(spacing.s0)

    ) {
        Row(
            modifier = Modifier
                .clip(shape.medium)
                .drawBehind {
                    drawRect(
                        color = colors.primary,
                        size = Size(spacing.s8.toPx(), size.height)
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = spacing.s16,
                        vertical = spacing.s12
                    ),
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMPlateBadge(
                        plate = item.plateCode,
                        size = sizing.plateBadgeMd
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    val style = reviewStatusStyle(item.reviewStatus, colors)
                    PMChip(
                        label = stringResource(style.label),
                        containerColor = style.background,
                        accentColor = style.foreground
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = item.createdAtText,
                        fontSize = fontSize.sm,
                        color = colors.textLabel
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(spacing.s4),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PMIcon(
                                imageVector = Icons.Filled.Star,
                                size = sizing.iconSm,
                                tint = colors.iconStar
                            )
                            PMText(
                                text = NumberFormatter.formatRating(item.ratingAverage),
                                fontSize = fontSize.md
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(spacing.s4),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PMIcon(
                                imageVector = Icons.AutoMirrored.Outlined.Chat,
                                tint = colors.iconDefault,
                                size = sizing.iconSm
                            )
                            PMText(
                                text = item.commentCount.toString(),
                                fontSize = fontSize.md
                            )
                        }
                        PMIcon(
                            imageVector = Icons.Filled.ChevronRight,
                            size = sizing.iconSm
                        )
                    }
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
    val spacing = PMTheme.spacing
    Column(
        modifier = Modifier.padding(spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        PlateReviewActivityCard(
            item = PlateReviewNotificationItem(
                id = "review_1",
                reviewId = 1L,
                normalizedPlateCode = "34AB1234",
                plateCode = "34 AB 1234",
                ratingAverage = 4.0,
                commentCount = 1,
                reviewStatus = ProfileReviewStatusUi.APPROVED,
                createdAtText = "2026-05-27",
                sortKey = "2026-05-27T10:00:00Z"
            ),
            onClick = { _, _ -> }
        )
        PlateReviewActivityCard(
            item = PlateReviewNotificationItem(
                id = "review_2",
                reviewId = 2L,
                normalizedPlateCode = "06XYZ06",
                plateCode = "06 XYZ 06",
                ratingAverage = 2.5,
                commentCount = 12,
                reviewStatus = ProfileReviewStatusUi.PENDING_REVIEW,
                createdAtText = "2026-05-26",
                sortKey = "2026-05-26T10:00:00Z"
            ),
            onClick = { _, _ -> }
        )
    }
}
