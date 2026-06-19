package com.mefy.platemate.presentation.features.main.discover

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.R

@StringRes
internal fun DiscoverFilterUi.toLabelResId(): Int {
    return when (this) {
        DiscoverFilterUi.Trend -> R.string.discover_filter_trend
        DiscoverFilterUi.Careless -> R.string.discover_filter_careless
        DiscoverFilterUi.GoodDriver -> R.string.discover_filter_good_driver
        DiscoverFilterUi.Newest -> R.string.discover_filter_new
    }
}

@StringRes
internal fun DiscoverFilterUi.toSectionTitleResId(): Int {
    return when (this) {
        DiscoverFilterUi.Trend -> R.string.discover_section_trend_plates
        DiscoverFilterUi.Careless -> R.string.discover_section_careless_plates
        DiscoverFilterUi.GoodDriver -> R.string.discover_section_good_driver_plates
        DiscoverFilterUi.Newest -> R.string.discover_section_new_plates
    }
}

internal fun DiscoverFilterUi.toSectionIcon(): ImageVector {
    return when (this) {
        DiscoverFilterUi.Trend -> Icons.Filled.Explore
        DiscoverFilterUi.Careless -> Icons.Filled.Warning
        DiscoverFilterUi.GoodDriver -> Icons.Filled.ThumbUp
        DiscoverFilterUi.Newest -> Icons.Filled.NewReleases
    }
}