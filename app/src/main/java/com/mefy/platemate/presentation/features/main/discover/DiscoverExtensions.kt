package com.mefy.platemate.presentation.features.main.discover

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiType

internal fun DiscoverMetricUiType.toMetricIcon(): ImageVector {
    return when (this) {
        DiscoverMetricUiType.Search -> Icons.Filled.Search
        DiscoverMetricUiType.Comment -> Icons.Filled.ChatBubble
        DiscoverMetricUiType.Alert -> Icons.Filled.Build
    }
}

@StringRes
internal fun DiscoverFilterUi.toLabelResId(): Int {
    return when (this) {
        DiscoverFilterUi.Trend -> R.string.discover_filter_trend
        DiscoverFilterUi.Attention -> R.string.discover_filter_careless
        DiscoverFilterUi.GoodDriver -> R.string.discover_filter_good_driver
        DiscoverFilterUi.Newest -> R.string.discover_filter_new
    }
}

@StringRes
internal fun DiscoverFilterUi.toSectionTitleResId(): Int {
    return when (this) {
        DiscoverFilterUi.Trend -> R.string.discover_section_trend_plates
        DiscoverFilterUi.Attention -> R.string.discover_section_attention_plates
        DiscoverFilterUi.GoodDriver -> R.string.discover_section_good_driver_plates
        DiscoverFilterUi.Newest -> R.string.discover_section_new_plates
    }
}

internal fun DiscoverFilterUi.toSectionIcon(): ImageVector {
    return when (this) {
        DiscoverFilterUi.Trend -> Icons.Filled.Explore
        DiscoverFilterUi.Attention -> Icons.Filled.Warning
        DiscoverFilterUi.GoodDriver -> Icons.Filled.ThumbUp
        DiscoverFilterUi.Newest -> Icons.Filled.NewReleases
    }
}

internal fun DiscoverFilterUi.toFilterChipIcon(): ImageVector {
    return when (this) {
        DiscoverFilterUi.Trend -> Icons.AutoMirrored.Filled.TrendingUp
        DiscoverFilterUi.Attention -> Icons.Filled.Warning
        DiscoverFilterUi.GoodDriver -> Icons.Filled.ThumbUp
        DiscoverFilterUi.Newest -> Icons.Filled.NewReleases
    }
}

internal fun RecentActivityActionType.toActivityIcon(): ImageVector {
    return when (this) {
        RecentActivityActionType.REVIEW_ADDED -> Icons.Filled.ChatBubble
        RecentActivityActionType.REPORT_ADDED -> Icons.Filled.Build
        RecentActivityActionType.STARRED -> Icons.Filled.Star
        RecentActivityActionType.SAVED -> Icons.Filled.Search
        RecentActivityActionType.CLAIMED -> Icons.Filled.Person
        RecentActivityActionType.UNKNOWN -> Icons.Filled.ChatBubble
    }
}
