package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class DiscoverForYouUiModel(
    val followedPlates: List<PlateDetailUiModel>,
    val savedPlates: List<PlateDetailUiModel>,
    val activities: List<DiscoverRecentActivityUiModel>,
    val weeklyStats: List<DiscoverMetricUiModel>
) {
    val hasContent: Boolean
        get() = followedPlates.isNotEmpty() || savedPlates.isNotEmpty() || activities.isNotEmpty()
}

@Immutable
data class DiscoverReportTypeOptionUi(
    val code: String,
    val label: String,
    val colorHex: String
)
