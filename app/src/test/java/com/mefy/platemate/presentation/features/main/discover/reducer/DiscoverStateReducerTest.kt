package com.mefy.platemate.presentation.features.main.discover.reducer

import com.mefy.platemate.presentation.features.main.discover.DiscoverFilterUi
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiState
import com.mefy.platemate.presentation.features.main.discover.mapper.DiscoverHomeUiData
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class DiscoverStateReducerTest {

    private val reducer = DiscoverStateReducer()

    @Test
    fun onContentLoaded_appliesMappedListsAndStopsLoading() {
        val home = DiscoverHomeUiData(
            metrics = listOf(
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Search,
                    valueText = "12",
                    labelResId = 1,
                    periodResId = 2
                )
            ),
            cityStats = listOf(
                DiscoverCityStatUiModel(
                    rank = 1,
                    cityName = "Istanbul",
                    count = 10,
                    progress = 1f
                )
            ),
            recentActivities = listOf(
                DiscoverRecentActivityUiModel(
                    id = "id",
                    type = com.mefy.platemate.domain.model.discovery.RecentActivityActionType.REVIEW_ADDED,
                    actorName = "fatih",
                    actionText = "REVIEW_ADDED",
                    plateCode = "34ABC123",
                    timeAgoText = "now"
                )
            ),
            tabs = com.mefy.platemate.domain.model.discovery.DiscoveryTabs(
                trendPlates = emptyList(),
                attentionPlates = emptyList(),
                goodDriverPlates = emptyList(),
                newPlates = emptyList()
            )
        )
        val plates = listOf(
            PlateDetailUiModel(
                id = "p1",
                rank = 1,
                plateCode = "34ABC123",
                cityName = null,
                ratingAverage = 4.2,
                commentCount = 3L
            )
        )

        val state = reducer.onContentLoaded(
            state = DiscoverUiState(isInitialLoading = true, isRefreshing = true),
            mappedHome = home,
            plateDetails = plates
        )

        assertFalse(state.isInitialLoading)
        assertFalse(state.isRefreshing)
        assertEquals(1, state.metrics.size)
        assertEquals(1, state.cityStats.size)
        assertEquals(1, state.recentActivities.size)
        assertEquals(1, state.plateDetail.size)
    }

    @Test
    fun onFilterSelected_updatesFilterAndPlateList() {
        val plates = listOf(
            PlateDetailUiModel(
                id = "p2",
                rank = 1,
                plateCode = "35DNG111",
                cityName = null,
                ratingAverage = 3.1,
                commentCount = 6L
            )
        )

        val state = reducer.onFilterSelected(
            state = DiscoverUiState(selectedFilter = DiscoverFilterUi.Trend),
            filter = DiscoverFilterUi.Attention,
            plateDetails = plates
        )

        assertEquals(DiscoverFilterUi.Attention, state.selectedFilter)
        assertEquals(1, state.plateDetail.size)
        assertEquals("35DNG111", state.plateDetail.first().plateCode)
    }
}
