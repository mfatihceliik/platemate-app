package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun DiscoverCityStatRow(
    cityStat: DiscoverCityStatUiModel,
    onClick: (cityId: Int, cityName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val rowClick = remember(cityStat.cityId, cityStat.cityName, onClick) {
        { onClick(cityStat.cityId, cityStat.cityName) }
    }

    PMCard(
        modifier = modifier.fillMaxWidth(),
        onClick = rowClick,
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(dims.sizing.rankBadgeSize)
                    .clip(RoundedCornerShape(dims.radius.r8))
                    .background(if (cityStat.rank == 1) colors.rankFirstBg else colors.rankOtherBg),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = cityStat.rank.toString(),
                    color = if (cityStat.rank == 1) colors.rankFirstFg else colors.rankOtherFg,
                    fontSize = dims.fontSize.md,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                PMText(
                    text = cityStat.cityName,
                    fontSize = dims.fontSize.md,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dims.spacing.s4)
                        .clip(RoundedCornerShape(dims.radius.r4))
                        .background(colors.outlineVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(cityStat.progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(dims.radius.r4))
                            .background(colors.primary)
                    )
                }
            }

            PMText(
                text = cityStat.count.toString(),
                fontSize = dims.fontSize.md,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary
            )
        }
    }
}

@Preview(name = "DiscoverCityStatRow", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverCityStatRowPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s8)) {
            DiscoverCityStatRow(
                cityStat = DiscoverCityStatUiModel(rank = 1, cityId = 34, cityName = "İstanbul", count = 42, progress = 1f),
                onClick = { _, _ -> }
            )
            DiscoverCityStatRow(
                cityStat = DiscoverCityStatUiModel(rank = 2, cityId = 6, cityName = "Ankara", count = 27, progress = 0.64f),
                onClick = { _, _ -> }
            )
        }
    }
}
