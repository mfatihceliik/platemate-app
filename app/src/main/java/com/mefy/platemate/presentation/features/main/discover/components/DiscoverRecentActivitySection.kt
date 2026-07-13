package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun DiscoverRecentActivityRow(
    activity: DiscoverRecentActivityUiModel,
    onPlateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val plateClick = remember(activity.plateCode, onPlateClick) {
        { onPlateClick(activity.plateCode) }
    }

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMAvatar(
                displayName = activity.actorName,
                size = dims.sizing.avatarSm
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = "${activity.actorName} ${activity.actionText.resolve()}",
                    fontSize = dims.fontSize.sm,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                PMText(
                    text = activity.timeAgoText.resolve(),
                    fontSize = dims.fontSize.xs,
                    color = colors.textTertiary
                )
            }

            PMChip(
                label = activity.plateCode,
                dense = true,
                onClick = plateClick
            )
        }
    }
}

@Preview(name = "DiscoverRecentActivityRow", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverRecentActivityRowPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s8)) {
            DiscoverRecentActivityRow(
                activity = DiscoverRecentActivityUiModel(
                    id = "1",
                    type = RecentActivityActionType.REVIEW_ADDED,
                    actorName = "fatih",
                    actionText = UiText.Resource(R.string.discover_activity_review_added),
                    plateCode = "34 EK 0682",
                    timeAgoText = UiText.Resource(R.string.time_ago_minutes, listOf(12L))
                ),
                onPlateClick = {}
            )
            DiscoverRecentActivityRow(
                activity = DiscoverRecentActivityUiModel(
                    id = "2",
                    type = RecentActivityActionType.REPORT_SUBMITTED,
                    actorName = "ayşe",
                    actionText = UiText.Resource(R.string.discover_activity_report_submitted),
                    plateCode = "06 ABC 123",
                    timeAgoText = UiText.Resource(R.string.time_ago_hours, listOf(3L))
                ),
                onPlateClick = {}
            )
        }
    }
}
