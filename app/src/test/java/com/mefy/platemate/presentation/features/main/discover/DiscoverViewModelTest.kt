package com.mefy.platemate.presentation.features.main.discover

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.discovery.CityStats
import com.mefy.platemate.domain.model.discovery.DailyStats
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.model.discovery.RecentActivity
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.domain.model.discovery.TopCityPlate
import com.mefy.platemate.domain.model.plate.PlateDetail
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.repository.DiscoveryRepository
import com.mefy.platemate.domain.repository.SavedPlateRepository
import com.mefy.platemate.domain.usecase.discovery.GetDiscoveryHomeUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.features.main.discover.mapper.DefaultDiscoverUiMapper
import com.mefy.platemate.presentation.features.main.discover.reducer.DiscoverStateReducer
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialLoad_populatesUiFromDiscoveryHome() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakeDiscoveryRepository(
            response = AppResult.Success(sampleDiscoveryHome())
        )
        val viewModel = createViewModel(repository = repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isInitialLoading)
        assertFalse(state.isRefreshing)
        assertEquals(DiscoverFilterUi.Trend, state.selectedFilter)
        assertEquals(3, state.metrics.size)
        assertEquals(2, state.plateDetail.size)
        assertEquals("Istanbul", state.plateDetail.first().cityName)
        assertTrue(state.plateDetail.first().reportTags.isNotEmpty())
        assertEquals(2, state.cityStats.size)
        assertEquals(2, state.recentActivities.size)
        assertEquals(1, repository.callCount)
        assertEquals(listOf(false), repository.forceRefreshRequests)
    }

    @Test
    fun filterSelected_updatesPlateDetailWithSelectedFilter() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(DiscoverUiAction.FilterSelected(DiscoverFilterUi.Careless))

        val state = viewModel.uiState.value
        assertEquals(DiscoverFilterUi.Careless, state.selectedFilter)
        assertEquals(1, state.plateDetail.size)
        assertTrue(state.plateDetail.all { it.plateCode.isNotBlank() })
    }

    @Test
    fun refreshRequested_triggersForceRefresh() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakeDiscoveryRepository(
            response = AppResult.Success(sampleDiscoveryHome())
        )
        val viewModel = createViewModel(repository = repository)
        advanceUntilIdle()

        viewModel.onAction(DiscoverUiAction.RefreshRequested)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isInitialLoading)
        assertFalse(state.isRefreshing)
        assertEquals(2, repository.callCount)
        assertEquals(listOf(false, true), repository.forceRefreshRequests)
    }

    @Test
    fun trendPlateClicked_emitsNavigateEffect() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val viewModel = createViewModel()
        val expectedTrendId = "trend_34_abc_123"

        val effectDeferred = async { viewModel.uiEffects.first() }
        runCurrent()

        viewModel.onAction(DiscoverUiAction.TrendPlateClicked(expectedTrendId))

        val effect = effectDeferred.await()
        assertEquals(
            DiscoverUiEffect.NavigateToTrendDetail(expectedTrendId),
            effect
        )
    }

    private fun createViewModel(
        repository: FakeDiscoveryRepository = FakeDiscoveryRepository(
            response = AppResult.Success(sampleDiscoveryHome())
        ),
        savedPlateRepository: SavedPlateRepository = FakeSavedPlateRepository()
    ): DiscoverViewModel {
        return DiscoverViewModel(
            getDiscoveryHomeUseCase = GetDiscoveryHomeUseCase(repository),
            observeSavedPlateCodesUseCase = ObserveSavedPlateCodesUseCase(savedPlateRepository),
            toggleSavedPlateUseCase = ToggleSavedPlateUseCase(savedPlateRepository),
            formatTurkishPlateInputUseCase = FormatTurkishPlateInputUseCase(),
            validateTurkishPlateUseCase = ValidateTurkishPlateUseCase(),
            discoverUiMapper = DefaultDiscoverUiMapper(
                formatTurkishPlateInputUseCase = FormatTurkishPlateInputUseCase(),
                validateTurkishPlateUseCase = ValidateTurkishPlateUseCase()
            ),
            discoverStateReducer = DiscoverStateReducer(),
            globalUiEventBus = DefaultGlobalUiEventBus()
        )
    }

    private class FakeSavedPlateRepository : SavedPlateRepository {
        override fun observeSavedPlates(): Flow<List<SavedPlate>> = flowOf(emptyList())
        override fun observeSavedPlateCodes(): Flow<Set<String>> = flowOf(emptySet())
        override suspend fun toggleSaved(plate: SavedPlate): Boolean = true
        override suspend fun replaceFromRemote(plates: List<SavedPlate>) = Unit
    }

    private fun sampleDiscoveryHome(): DiscoveryHome = DiscoveryHome(
        dailyStats = DailyStats(
            todaySearchCount = 12L,
            todayReviewCount = 8L,
            todayReportCount = 3L
        ),
        tabs = DiscoveryTabs(
            trendPlates = listOf(
                samplePlate("34ABC123", score = 9.2),
                samplePlate("06XYZ987", score = 8.4)
            ),
            attentionPlates = listOf(
                samplePlate("35DNG111", score = 7.1)
            ),
            goodDriverPlates = listOf(
                samplePlate("16GOOD16", score = 9.8)
            ),
            newPlates = listOf(
                samplePlate("34NEW001", score = 6.9)
            )
        ),
        cityStats = listOf(
            CityStats(cityId = 34, cityName = "Istanbul", todayReviewCount = 10L),
            CityStats(cityId = 6, cityName = "Ankara", todayReviewCount = 4L)
        ),
        topCityPlates = listOf(
            TopCityPlate(
                plateCode = "34ABC123",
                todayReviewCount = 8L,
                todayReportCount = 1L,
                lastActivityAt = "2026-05-18T08:30:00",
                ratingAverage = 4.8,
                reviewCount = 18L
            )
        ),
        recentActivities = listOf(
            RecentActivity(
                username = "fatih",
                plateCode = "34ABC123",
                actionType = RecentActivityActionType.REVIEW_ADDED,
                occurredAt = "2026-05-18T10:00:00",
                rating = 4.0,
                comment = "iyi",
                reportTypeCode = "",
                reportTypeLabel = ""
            ),
            RecentActivity(
                username = "ali",
                plateCode = "06XYZ987",
                actionType = RecentActivityActionType.REPORT_ADDED,
                occurredAt = "2026-05-18T09:00:00",
                rating = 0.0,
                comment = "",
                reportTypeCode = "DANGER",
                reportTypeLabel = "Danger"
            )
        )
    )

    private fun samplePlate(plateCode: String, score: Double): PlateDetail = PlateDetail(
        plateCode = plateCode,
        cityName = "Istanbul",
        ratingAverage = 4.4,
        reviewCount = 5L,
        weeklySearchCount = 7L,
        todayReviewCount = 3L,
        todayReportCount = 1L,
        todayWeightedReportScore = 1.5,
        score = score,
        lastActivityAt = "2026-05-18T10:00:00",
        topReportType = listOf(
            ReportType(
                code = "DANGER",
                label = "Danger",
                description = "Danger behavior",
                iconKey = "alert",
                severity = "HIGH",
                colorHex = "#FF0000",
                weight = 10,
                sortOrder = 1
            )
        )
    )

    private class FakeDiscoveryRepository(
        private val response: AppResult<DiscoveryHome>
    ) : DiscoveryRepository {
        val forceRefreshRequests = mutableListOf<Boolean>()
        var callCount = 0

        override suspend fun getDiscoveryHome(forceRefresh: Boolean): AppResult<DiscoveryHome> {
            callCount++
            forceRefreshRequests += forceRefresh
            return response
        }
    }
}
