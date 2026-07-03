package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMPlateCard(
    id: String,
    rank: Int,
    cityCode: String,
    plateNumber: String,
    rating: String,
    commentCount: Long,
    searchCount: Long,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    // Wrap the id-aware callback once, keyed on stable inputs, so PMCard's
    // () -> Unit stays referentially stable across recompositions.
    val cardClick = remember(id, onClick) { { onClick(id) } }

    PMCard(
        modifier = modifier,
        onClick = cardClick,
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isFirst = rank == 1
            Box(
                modifier = Modifier
                    .size(dims.sizing.rankBadgeSize)
                    .clip(RoundedCornerShape(dims.radius.r8))
                    .background(if (isFirst) colors.rankFirstBg else colors.rankOtherBg),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = rank.toString(),
                    color = if (isFirst) colors.rankFirstFg else colors.rankOtherFg,
                    fontSize = dims.fontSize.md,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            PMPlateBadge(cityCode = cityCode, size = PlateBadgeSize.Medium)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                Text(
                    text = plateNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlateMetric(icon = Icons.Filled.Star, value = rating, iconTint = colors.star)
                    PlateMetric(
                        icon = Icons.AutoMirrored.Outlined.Chat,
                        value = formatCount(commentCount),
                        iconTint = colors.textTertiary
                    )
                    if (searchCount > 0) {
                        PlateMetric(
                            icon = Icons.Outlined.Search,
                            value = formatCount(searchCount),
                            iconTint = colors.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlateMetric(icon: ImageVector, value: String, iconTint: Color) {
    val dims = MaterialTheme.pmDimensions
    Row(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMIcon(imageVector = icon, contentDescription = null, size = dims.sizing.iconSm, tint = iconTint)
        PMText(
            text = value,
            fontSize = dims.fontSize.sm,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.pmColors.textSecondary
        )
    }
}

/** Compact count: 1234 -> "1.2B" (bin), 1_200_000 -> "1.2M". */
private fun formatCount(value: Long): String = when {
    value >= 1_000_000 -> trimZero(value / 1_000_000.0) + "M"
    value >= 1_000 -> trimZero(value / 1_000.0) + "B"
    else -> value.toString()
}

private fun trimZero(value: Double): String {
    val rounded = (value * 10).toLong() / 10.0
    return if (rounded % 1.0 == 0.0) rounded.toLong().toString() else String.format("%.1f", rounded)
}

@Preview(name = "PMTrendCard", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPlateCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.pmColors.background)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMPlateCard(id = "1", rank = 1, cityCode = "34", plateNumber = "34 EK 0682", rating = "4.8", commentCount = 12, searchCount = 1240, onClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(id = "2", rank = 2, cityCode = "06", plateNumber = "06 ABC 123", rating = "4.6", commentCount = 9, searchCount = 910, onClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(id = "3", rank = 3, cityCode = "35", plateNumber = "35 T 4421", rating = "4.3", commentCount = 6, searchCount = 0, onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
