package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.components.PMPlateCard
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle
import com.mefy.platemate.presentation.features.uimodel.DiscoverForYouUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun DiscoverForYouSection(
    forYou: DiscoverForYouUiModel,
    onPlateClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onActivityPlateClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    cardStyle: PMPlateCardStyle = PMPlateCardStyle.Classic
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        PMSectionLabel(text = stringResource(R.string.discover_for_you_title))

        if (forYou.weeklyStats.isNotEmpty()) {
            DiscoverHeroStats(metrics = forYou.weeklyStats)
        }

        if (!forYou.hasContent) {
            PMText(
                text = stringResource(R.string.discover_for_you_empty),
                fontSize = dims.fontSize.md,
                color = colors.textTertiary
            )
            return@Column
        }

        if (forYou.followedPlates.isNotEmpty()) {
            PMText(
                text = stringResource(R.string.discover_for_you_followed),
                fontSize = dims.fontSize.md,
                color = colors.textSecondary
            )
            ForYouPlateRow(
                plates = forYou.followedPlates,
                cardStyle = cardStyle,
                onPlateClick = onPlateClick,
                onBookmarkClick = onBookmarkClick
            )
        }

        if (forYou.savedPlates.isNotEmpty()) {
            PMText(
                text = stringResource(R.string.discover_for_you_saved),
                fontSize = dims.fontSize.md,
                color = colors.textSecondary
            )
            ForYouPlateRow(
                plates = forYou.savedPlates,
                cardStyle = cardStyle,
                onPlateClick = onPlateClick,
                onBookmarkClick = onBookmarkClick
            )
        }

        if (forYou.activities.isNotEmpty()) {
            PMText(
                text = stringResource(R.string.discover_for_you_activity),
                fontSize = dims.fontSize.md,
                color = colors.textSecondary
            )
            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                forYou.activities.forEach { activity ->
                    DiscoverRecentActivityRow(
                        activity = activity,
                        onPlateClick = onActivityPlateClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ForYouPlateRow(
    plates: List<PlateDetailUiModel>,
    cardStyle: PMPlateCardStyle,
    onPlateClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit
) {
    val dims = MaterialTheme.pmDimensions

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        itemsIndexed(items = plates, key = { index, it -> "${it.id}_$index" }, contentType = { _, _ -> "for_you_plate" }) { _, plate ->
            PMPlateCard(
                id = plate.id,
                rank = plate.rank ?: 0,
                plateNumber = plate.plateCode,
                rating = plate.ratingText,
                commentCount = plate.commentCount,
                searchCount = plate.searchCount,
                onClick = onPlateClick,
                style = cardStyle,
                isBookmarked = plate.isBookmarked,
                onBookmarkClick = onBookmarkClick,
                modifier = Modifier.fillParentMaxWidth(0.88f)
            )
        }
    }
}

@Preview(name = "DiscoverForYouSection", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverForYouSectionPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverForYouSection(
            forYou = DiscoverForYouUiModel(
                followedPlates = listOf(
                    PlateDetailUiModel(
                        id = "34EK0682_ForYouFollowed",
                        rank = 1,
                        plateCode = "34 EK 0682",
                        cityName = "İstanbul",
                        reportTags = emptyList(),
                        ratingAverage = 4.8,
                        commentCount = 12,
                        cityCode = "34",
                        ratingText = "4.8",
                        isBookmarked = true
                    )
                ),
                savedPlates = emptyList(),
                activities = listOf(
                    DiscoverRecentActivityUiModel(
                        id = "a1",
                        type = RecentActivityActionType.REVIEW_ADDED,
                        actorName = "fatih",
                        actionText = UiText.Resource(R.string.discover_activity_review_added),
                        plateCode = "34 EK 0682",
                        timeAgoText = UiText.Resource(R.string.time_ago_hours, listOf(2L))
                    )
                ),
                weeklyStats = listOf(
                    DiscoverMetricUiModel(
                        type = DiscoverMetricUiType.Search,
                        valueText = "412",
                        labelResId = R.string.discover_metric_search_label,
                        periodResId = R.string.discover_metric_week_period,
                        deltaText = "+18%",
                        deltaPositive = true
                    )
                )
            ),
            onPlateClick = {},
            onBookmarkClick = {},
            onActivityPlateClick = {}
        )
    }
}
