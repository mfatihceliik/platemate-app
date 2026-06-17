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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMTrendCard(
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
                    .background(if (isFirst) colors.rankFirstBg else colors.rankOtherBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = if (isFirst) colors.rankFirstFg else colors.rankOtherFg,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            PMPlateBadge(cityCode = cityCode, size = PlateBadgeSize.Medium)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = plateNumber,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = colors.star,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "$rating · $extra",
                        fontSize = 12.sp,
                        color = colors.textTertiary
                    )
                }
            }
        }
    }
}

@Preview(name = "PMTrendCard", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMTrendCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PMTrendCard(rank = 1, cityCode = "34", plateNumber = "34 EK 0682", rating = "4.8", extra = "12.4B", onClick = {}, modifier = Modifier.fillMaxWidth())
            PMTrendCard(rank = 2, cityCode = "06", plateNumber = "06 ABC 123", rating = "4.6", extra = "9.1B", onClick = {}, modifier = Modifier.fillMaxWidth())
            PMTrendCard(rank = 3, cityCode = "35", plateNumber = "35 T 4421", rating = "4.3", extra = "6.7B", onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
