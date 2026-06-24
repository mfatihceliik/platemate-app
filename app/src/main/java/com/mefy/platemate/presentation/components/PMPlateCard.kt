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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMPlateCard(
    rank: Int,
    cityCode: String,
    plateNumber: String,
    rating: String,
    extra: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier,
        onClick = onClick,
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
                    .background(if (isFirst) MaterialTheme.pmColors.rankFirstBg else MaterialTheme.pmColors.rankOtherBg),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = rank.toString(),
                    color = if (isFirst) MaterialTheme.pmColors.rankFirstFg else MaterialTheme.pmColors.rankOtherFg,
                    fontSize = dims.fontSize.md,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            PMPlateBadge(cityCode = cityCode, size = PlateBadgeSize.Medium)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                Text(
                    text = plateNumber,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.pmColors.textPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                ) {
                    PMIcon(
                        imageVector = Icons.Filled.Star,
                        tint = MaterialTheme.pmColors.star,
                    )
                    PMText(
                        text = "$rating · $extra",
                        color = MaterialTheme.pmColors.textTertiary
                    )
                }
            }
        }
    }
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
            PMPlateCard(rank = 1, cityCode = "34", plateNumber = "34 EK 0682", rating = "4.8", extra = "12.4B", onClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(rank = 2, cityCode = "06", plateNumber = "06 ABC 123", rating = "4.6", extra = "9.1B", onClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(rank = 3, cityCode = "35", plateNumber = "35 T 4421", rating = "4.3", extra = "6.7B", onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
