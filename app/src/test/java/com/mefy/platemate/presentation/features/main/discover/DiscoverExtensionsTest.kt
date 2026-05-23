package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import com.mefy.platemate.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DiscoverExtensionsTest {

    @Test
    fun toSectionTitleResId_returnsExpectedResForAllFilters() {
        assertEquals(R.string.discover_section_trend_plates, DiscoverFilterUi.Trend.toSectionTitleResId())
        assertEquals(R.string.discover_section_attention_plates, DiscoverFilterUi.Attention.toSectionTitleResId())
        assertEquals(R.string.discover_section_good_driver_plates, DiscoverFilterUi.GoodDriver.toSectionTitleResId())
        assertEquals(R.string.discover_section_new_plates, DiscoverFilterUi.Newest.toSectionTitleResId())
    }

    @Test
    fun toSectionIcon_returnsExpectedIconForAllFilters() {
        assertTrue(DiscoverFilterUi.Trend.toSectionIcon() === Icons.Filled.Explore)
        assertTrue(DiscoverFilterUi.Attention.toSectionIcon() === Icons.Filled.Warning)
        assertTrue(DiscoverFilterUi.GoodDriver.toSectionIcon() === Icons.Filled.ThumbUp)
        assertTrue(DiscoverFilterUi.Newest.toSectionIcon() === Icons.Filled.NewReleases)
    }
}
