package com.mefy.platemate.presentation.features.main.search

import com.mefy.platemate.data.repository.InMemoryRecentSearchRepository
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.repository.PlateRepository
import com.mefy.platemate.domain.repository.SavedPlateRepository
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.ClearRecentSearchesUseCase
import com.mefy.platemate.domain.usecase.search.DeleteRecentSearchUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ObserveRecentSearchesUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.domain.usecase.search.UpsertRecentSearchUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.common.error.DefaultUiErrorResolver
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.features.main.search.mapper.DefaultSearchUiMapper
import com.mefy.platemate.presentation.features.main.search.reducer.SearchStateReducer
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialState_startsWithoutMockRecentData() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val viewModel = createViewModel()
        runCurrent()
        val state = viewModel.uiState.value

        assertFalse(state.isInitialLoading)
        assertEquals("", state.plateInput)
        assertFalse(state.isPlateValid)
        assertFalse(state.isSearchEnabled)
        assertTrue(state.recentSearches.isEmpty())
    }

    @Test
    fun initialState_isInitialLoadingBeforeFirstRecentEmission() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val viewModel = createViewModel()

        assertTrue(viewModel.uiState.value.isInitialLoading)
    }

    @Test
    fun plateInputChanged_formatsAndValidatesPlate() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val viewModel = createViewModel()
        runCurrent()

        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))

        val state = viewModel.uiState.value
        assertEquals("34 ABC 123", state.plateInput)
        assertTrue(state.isPlateValid)
        assertTrue(state.isSearchEnabled)
    }

    @Test
    fun searchClicked_validAndSuccessful_updatesRecentAndEmitsNavigationEffect() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakePlateRepository(
            result = AppResult.Success(
                samplePlateSearchResult(
                    id = 10L,
                    plateCode = "34ABC123",
                    cityName = "Istanbul",
                    ratingAverage = 4.3,
                    totalReviewCount = 12L,
                    totalRatingSum = 52L
                )
            )
        )
        val viewModel = createViewModel(repository = repository)
        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))
        runCurrent()

        val effectDeferred = async { viewModel.uiEffect.first() }
        runCurrent()

        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val effect = effectDeferred.await()

        assertEquals(1, repository.callCount)
        assertEquals("34ABC123", repository.lastPlateCode)
        assertEquals(1, state.recentSearches.size)
        assertEquals("34 ABC 123", state.recentSearches.first().plateCode)
        assertFalse(state.recentSearches.first().isBookmarked)
        assertEquals(UiActionState.Idle, state.submitState)
        assertEquals(SearchUiEffect.NavigateToSearchDetail("34ABC123"), effect)
    }

    @Test
    fun searchClicked_invalidInput_doesNotCallApi() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakePlateRepository()
        val viewModel = createViewModel(repository = repository)

        viewModel.onAction(SearchUiAction.PlateInputChanged("34 A 123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0, repository.callCount)
        assertFalse(state.isPlateValid)
        assertTrue(state.recentSearches.isEmpty())
    }

    @Test
    fun searchClicked_apiError_setsInlineFormMessageAndKeepsRecentUntouched() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakePlateRepository(
            result = AppResult.Error(AppError.Network("offline"))
        )
        val viewModel = createViewModel(repository = repository)

        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, repository.callCount)
        assertTrue(state.recentSearches.isEmpty())
        assertNotNull(state.formMessage)
        assertTrue(state.submitState is UiActionState.Error)
    }

    @Test
    fun duplicateSuccessfulSearch_keepsSingleRecentEntry() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakePlateRepository(
            result = AppResult.Success(
                samplePlateSearchResult(
                    id = 10L,
                    plateCode = "34ABC123",
                    cityName = "Istanbul",
                    ratingAverage = 4.3,
                    totalReviewCount = 12L,
                    totalRatingSum = 52L
                )
            )
        )
        val viewModel = createViewModel(repository = repository)

        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        viewModel.onAction(SearchUiAction.PlateInputChanged("34 abc 123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, repository.callCount)
        assertEquals(1, state.recentSearches.size)
    }

    @Test
    fun recentBookmarkClicked_togglesBookmarkFlag() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakePlateRepository(
            result = AppResult.Success(samplePlateSearchResult())
        )
        val savedRepository = FakeSavedPlateRepository()
        val viewModel = createViewModel(
            repository = repository,
            savedPlateRepository = savedRepository
        )

        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        val normalizedPlate = viewModel.uiState.value.recentSearches.first().normalizedPlateCode
        viewModel.onAction(SearchUiAction.RecentBookmarkClicked(normalizedPlate))
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.recentSearches.first().isBookmarked)

        viewModel.onAction(SearchUiAction.RecentBookmarkClicked(normalizedPlate))
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.recentSearches.first().isBookmarked)
    }

    @Test
    fun clearRecentClicked_clearsRecentList() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakePlateRepository(
            result = AppResult.Success(samplePlateSearchResult())
        )
        val viewModel = createViewModel(repository = repository)
        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.recentSearches.size)

        viewModel.onAction(SearchUiAction.ClearRecentClicked)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.recentSearches.isEmpty())
    }

    @Test
    fun recentDismissClicked_removesOnlyTargetRecentItem() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = FakePlateRepository(result = AppResult.Success(samplePlateSearchResult()))
        val recentSearchRepository = InMemoryRecentSearchRepository()
        val viewModel = createViewModel(
            repository = repository,
            recentSearchRepository = recentSearchRepository
        )

        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        recentSearchRepository.upsertRecent(
            com.mefy.platemate.domain.model.search.RecentSearch(
                normalizedPlateCode = "06XYZ987",
                formattedPlateCode = "06 XYZ 987",
                cityName = "Ankara",
                ratingAverage = 3.9,
                commentCount = 7L,
                reportTypes = emptyList()
            )
        )
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.recentSearches.size)
        viewModel.onAction(SearchUiAction.RecentDismissClicked("06XYZ987"))
        advanceUntilIdle()

        val recent = viewModel.uiState.value.recentSearches
        assertEquals(1, recent.size)
        assertEquals("34ABC123", recent.first().normalizedPlateCode)
    }

    private fun createViewModel(
        repository: FakePlateRepository = FakePlateRepository(),
        recentSearchRepository: InMemoryRecentSearchRepository = InMemoryRecentSearchRepository(),
        savedPlateRepository: FakeSavedPlateRepository = FakeSavedPlateRepository()
    ): SearchViewModel = SearchViewModel(
        searchPlateUseCase = SearchPlateUseCase(repository),
        observeRecentSearchesUseCase = ObserveRecentSearchesUseCase(recentSearchRepository),
        observeSavedPlateCodesUseCase = ObserveSavedPlateCodesUseCase(savedPlateRepository),
        upsertRecentSearchUseCase = UpsertRecentSearchUseCase(recentSearchRepository),
        toggleSavedPlateUseCase = ToggleSavedPlateUseCase(savedPlateRepository),
        deleteRecentSearchUseCase = DeleteRecentSearchUseCase(recentSearchRepository),
        clearRecentSearchesUseCase = ClearRecentSearchesUseCase(recentSearchRepository),
        formatTurkishPlateInputUseCase = FormatTurkishPlateInputUseCase(),
        validateTurkishPlateUseCase = ValidateTurkishPlateUseCase(),
        searchUiMapper = DefaultSearchUiMapper(),
        searchStateReducer = SearchStateReducer(),
        uiErrorResolver = DefaultUiErrorResolver()
    )

    private fun samplePlateSearchResult(
        plateCode: String = "34ABC123",
        cityName: String = "Istanbul",
        ratingAverage: Double = 4.2,
        reviewCount: Long = 3L
    ): PlateSearchResult = PlateSearchResult(
        plateCode = plateCode,
        cityName = cityName,
        ratingAverage = ratingAverage,
        reviewCount = reviewCount,
        todaySearchCount = 0L,
        todayReviewCount = 0L,
        todayReportCount = 0L,
        todayWeightedReportScore = 0.0,
        score = 0.0,
        lastActivityAt = "2026-05-19T00:00:00Z",
        topReportTypes = listOf(
            ReportType(
                code = "SAFE",
                label = "Safe",
                description = "Safe driving",
                iconKey = "shield",
                severity = "LOW",
                colorHex = "#00AA00",
                weight = 1,
                sortOrder = 1
            )
        )
    )

    private class FakeSavedPlateRepository : SavedPlateRepository {
        private val savedPlates = MutableStateFlow<List<SavedPlate>>(emptyList())
        private val savedCodes = MutableStateFlow<Set<String>>(emptySet())

        override fun observeSavedPlates(): Flow<List<SavedPlate>> = savedPlates.asStateFlow()

        override fun observeSavedPlateCodes(): Flow<Set<String>> = savedCodes.asStateFlow()

        override suspend fun toggleSaved(plate: SavedPlate): Boolean {
            val normalizedPlateCode = plate.normalizedPlateCode
            return if (savedCodes.value.contains(normalizedPlateCode)) {
                savedCodes.value = savedCodes.value - normalizedPlateCode
                savedPlates.value = savedPlates.value.filterNot {
                    it.normalizedPlateCode == normalizedPlateCode
                }
                false
            } else {
                savedCodes.value = savedCodes.value + normalizedPlateCode
                savedPlates.value = listOf(plate) + savedPlates.value.filterNot {
                    it.normalizedPlateCode == normalizedPlateCode
                }
                true
            }
        }
    }

    private class FakePlateRepository(
        private val result: AppResult<PlateSearchResult> = AppResult.Success(
            PlateSearchResult(
                plateCode = "34ABC123",
                cityName = "Istanbul",
                ratingAverage = 4.5,
                reviewCount = 15,
                todaySearchCount = 5,
                todayReviewCount = 2,
                todayReportCount = 1,
                todayWeightedReportScore = 3.0,
                score = 0.0,
                lastActivityAt = "2026-05-19T00:00:00Z",
                recentReportTypes = listOf(
                    ReportType(
                        code = "SAFE",
                        label = "Safe",
                        description = "Safe driving",
                        iconKey = "shield",
                        severity = "LOW",
                        colorHex = "#00AA00",
                        weight = 1,
                        sortOrder = 1
                    )
                )
            )
        )
    ) : PlateRepository {
        var callCount: Int = 0
        var lastPlateCode: String? = null

        override suspend fun searchPlate(plateCode: String): AppResult<PlateSearchResult> {
            callCount++
            lastPlateCode = plateCode
            return result
        }
    }
}

