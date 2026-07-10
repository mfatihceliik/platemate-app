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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.SearchRecentUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMPlateCompactCard(
    modifier: Modifier = Modifier,
    item: SearchRecentUiModel,
    trailingIcon: ImageVector,
    trailingIconTint: Color,
    onClick: (String) -> Unit,
    onTrailingIconClick: (String) -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMCard(
        modifier = modifier.wrapContentSize(),
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .debouncedClickable { onClick(item.plateCode) },
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(
                    plate = item.plateCode,
                    size = dims.sizing.plateBadgeSm
                )
                PMIconButton(
                    imageVector = trailingIcon,
                    iconColor = trailingIconTint,
                    onClick = { onTrailingIconClick(item.normalizedPlateCode) }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4)
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
                        fontSize = dims.fontSize.md,
                        maxLines = 1,
                        color = colors.textTertiary
                    )
                }
                if (item.cityName != null) {
                    PMText(
                        text = "· ${item.cityName}",
                        fontSize = dims.fontSize.md,
                        color = colors.textLabel,
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
    val colors = MaterialTheme.pmColors
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Saved (bookmark) varyantı
        PMPlateCompactCard(
            item = previewItem(),
            trailingIcon = Icons.Filled.Bookmark,
            trailingIconTint = colors.primary,
            onClick = {},
            onTrailingIconClick = {}
        )
        // Alarm varyantı
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
