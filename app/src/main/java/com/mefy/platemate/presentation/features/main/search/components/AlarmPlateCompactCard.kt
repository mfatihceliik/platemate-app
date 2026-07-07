package com.mefy.platemate.presentation.features.main.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.SearchRecentUiModel
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun AlarmPlateCompactCard(
    item: SearchRecentUiModel,
    onClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMCard(
        modifier = Modifier.width(dims.sizing.savedPlateCardWidth),
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .debouncedClickable { onClick(item.plateCode) },
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(
                    cityCode = item.plateCode.take(2),
                    size = PlateBadgeSize.Small
                )
                PMIcon(
                    imageVector = Icons.Filled.NotificationsActive,
                    tint = colors.primary,
                    size = dims.sizing.iconXl,
                    modifier = Modifier.debouncedClickable { onRemoveClick(item.normalizedPlateCode) }
                )
            }

            PMText(
                text = item.plateCode,
                fontSize = dims.fontSize.lg,
                color = colors.textPrimary,
                overflow = TextOverflow.Ellipsis
            )

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
                        tint = colors.star,
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
