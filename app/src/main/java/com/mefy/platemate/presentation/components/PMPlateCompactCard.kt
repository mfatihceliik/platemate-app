package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.common.formatter.NumberFormatter
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.SearchRecentUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMPlateCompactCard(
    modifier: Modifier = Modifier,
    item: SearchRecentUiModel,
    trailingIcon: ImageVector,
    trailingIconTint: Color,
    onClick: (String) -> Unit,
    onTrailingIconClick: (String) -> Unit
) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val fontSize = PMTheme.fontSize
    val colors = PMTheme.colors

    val onItemClick = remember(item.plateCode, onClick) { { onClick(item.plateCode) } }
    val onTrailingIconClick = remember(item.normalizedPlateCode, onTrailingIconClick) { { onTrailingIconClick(item.normalizedPlateCode) } }

    PMCard(
        modifier = modifier.wrapContentSize(),
        padding = PaddingValues(spacing.s12)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .debouncedClickable { onItemClick() },
            verticalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(spacing.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(
                    plate = item.plateCode,
                    size = sizing.plateBadgeSm
                )
                PMIconButton(
                    imageVector = trailingIcon,
                    iconColor = trailingIconTint,
                    onClick = onTrailingIconClick
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.s4)
            ) {
                if (item.ratingAverage > 0) {
                    val ratingText = remember(item.ratingAverage) {
                        NumberFormatter.formatRating(item.ratingAverage)
                    }
                    PMIcon(
                        imageVector = Icons.Filled.Star,
                        tint = colors.iconStar,
                    )
                    PMText(
                        text = ratingText,
                        fontSize = fontSize.md,
                        maxLines = 1,
                        color = colors.textPrimary
                    )
                }
                if (item.cityName != null) {
                    PMText(
                        text = "· ${item.cityName}",
                        fontSize = fontSize.md,
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(name = "PMPlateCompactCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPlateCompactCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMPlateCompactCardPreviewContent()
    }
}

@Preview(name = "PMPlateCompactCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMPlateCompactCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMPlateCompactCardPreviewContent()
    }
}

@Composable
private fun PMPlateCompactCardPreviewContent() {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    Row(
        modifier = Modifier.padding(spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        PMPlateCompactCard(
            item = previewItem(),
            trailingIcon = Icons.Filled.Bookmark,
            trailingIconTint = colors.primary,
            onClick = {},
            onTrailingIconClick = {}
        )
        PMPlateCompactCard(
            item = previewItem().copy(
                normalizedPlateCode = "06ABC123",
                plateCode = "06 ABC 123",
                cityName = "Ankara",
                ratingAverage = 0.0
            ),
            trailingIcon = Icons.Filled.NotificationsActive,
            trailingIconTint = colors.primary,
            onClick = {},
            onTrailingIconClick = {}
        )
    }
}

private fun previewItem() = SearchRecentUiModel(
    normalizedPlateCode = "34EK0682",
    plateCode = "34 EK 0682",
    cityName = "İstanbul",
    reportTags = emptyList(),
    ratingAverage = 4.6,
    commentCount = 12,
    isBookmarked = true
)
