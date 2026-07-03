package com.mefy.platemate.presentation.features.main.search

import com.mefy.platemate.data.repository.InMemoryRecentSearchRepository
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.core.connectivity.NetworkMonitor
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.data.remote.dto.admin.PlateRemovalRequestDto
import com.mefy.platemate.data.remote.dto.plate.AddPlateRemovalRequestRequest
import com.mefy.platemate.data.remote.dto.plate.MyPlateListsDto
import com.mefy.platemate.data.remote.dto.plate.PlateSearchResponseDto
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.repository.AlarmPlateRepository
import com.mefy.platemate.domain.repository.PlateRepository
import com.mefy.platemate.domain.repository.SavedPlateRepository
import com.mefy.platemate.domain.usecase.alarm.ObserveAlarmPlatesUseCase
import com.mefy.platemate.domain.usecase.alarm.SyncMyPlateListsUseCase
import com.mefy.platemate.domain.usecase.alarm.ToggleAlarmPlateUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlatesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.ClearRecentSearchesUseCase
import com.mefy.platemate.domain.usecase.search.DeleteRecentSearchUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ObserveRecentSearchesUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.domain.usecase.search.UpsertRecentSearchUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.features.main.search.mapper.DefaultSearchUiMapper
import com.mefy.platemate.presentation.features.main.search.reducer.SearchStateReducer
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

        assertEquals("", state.plateInput)
        assertFalse(state.isPlateValid)
        assertFalse(state.isSearchEnabled)
        assertTrue(state.recentSearches.isEmpty())
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
        assertFalse(state.isSearching)
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
            result = AppResult.Error(AppError.Network())
        )
        val viewModel = createViewModel(repository = repository)

        viewModel.onAction(SearchUiAction.PlateInputChanged("34abc123"))
        viewModel.onAction(SearchUiAction.SearchClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, repository.callCount)
        assertTrue(state.recentSearches.isEmpty())
        assertFalse(state.isSearching)
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
        savedPlateRepository: FakeSavedPlateRepository = FakeSavedPlateRepository(),
        alarmPlateRepository: FakeAlarmPlateRepository = FakeAlarmPlateRepository()
    ): SearchViewModel = SearchViewModel(
        searchPlateUseCase = SearchPlateUseCase(repository),
        observeRecentSearchesUseCase = ObserveRecentSearchesUseCase(recentSearchRepository),
        observeSavedPlatesUseCase = ObserveSavedPlatesUseCase(savedPlateRepository),
        observeSavedPlateCodesUseCase = ObserveSavedPlateCodesUseCase(savedPlateRepository),
        observeAlarmPlatesUseCase = ObserveAlarmPlatesUseCase(alarmPlateRepository),
        toggleAlarmPlateUseCase = ToggleAlarmPlateUseCase(alarmPlateRepository),
        syncMyPlateListsUseCase = SyncMyPlateListsUseCase(
            plateApiService = FakePlateApiService,
            savedPlateRepository = savedPlateRepository,
            alarmPlateRepository = alarmPlateRepository,
            formatTurkishPlateInputUseCase = FormatTurkishPlateInputUseCase()
        ),
        upsertRecentSearchUseCase = UpsertRecentSearchUseCase(recentSearchRepository),
        toggleSavedPlateUseCase = ToggleSavedPlateUseCase(savedPlateRepository),
        deleteRecentSearchUseCase = DeleteRecentSearchUseCase(recentSearchRepository),
        clearRecentSearchesUseCase = ClearRecentSearchesUseCase(recentSearchRepository),
        formatTurkishPlateInputUseCase = FormatTurkishPlateInputUseCase(),
        validateTurkishPlateUseCase = ValidateTurkishPlateUseCase(),
        searchUiMapper = DefaultSearchUiMapper(),
        searchStateReducer = SearchStateReducer(),
        networkMonitor = FakeOnlineNetworkMonitor,
        appDispatchers = AppDispatchers(
            main = mainDispatcherRule.dispatcher,
            io = mainDispatcherRule.dispatcher,
            default = mainDispatcherRule.dispatcher
        ),
        globalUiEventBus = DefaultGlobalUiEventBus()
    )

    private object FakeOnlineNetworkMonitor : NetworkMonitor {
        override val isOnline: Flow<Boolean> = flowOf(true)
        override fun isCurrentlyOnline(): Boolean = true
    }

    private fun samplePlateSearchResult(
        id: Long = 1L,
        plateCode: String = "34ABC123",
        cityName: String = "Istanbul",
        ratingAverage: Double = 4.2,
        reviewCount: Long = 3L,
        totalReviewCount: Long = 0L,
        totalRatingSum: Long = 0L
    ): PlateSearchResult = PlateSearchResult(
        id = id,
        plateCode = plateCode,
        cityName = cityName,
        ratingAverage = ratingAverage,
        reviewCount = reviewCount,
        ratingDistribution = emptyList(),
        tagSummary = emptyList(),
        recentReviews = emptyList(),
        totalSearchCount = 0L,
        totalReviewCount = totalReviewCount,
        totalReportCount = 0L,
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

    private object FakePlateApiService : PlateApiService {
        // getMyLists boş liste döner: sync no-op olur, testler alarm/saved senkronundan etkilenmez.
        override suspend fun getMyLists(): DataResultResponse<MyPlateListsDto> =
            DataResultResponse(message = null, success = true, data = MyPlateListsDto(savedPlates = null, alarmPlates = null))

        override suspend fun searchPlate(plate: String): DataResultResponse<PlateSearchResponseDto> =
            throw UnsupportedOperationException("not used in tests")

        override suspend fun followPlate(plateCode: String): ResultResponse = ResultResponse(null, true)
        override suspend fun unfollowPlate(plateCode: String): ResultResponse = ResultResponse(null, true)
        override suspend fun savePlate(plateCode: String): ResultResponse = ResultResponse(null, true)
        override suspend fun unsavePlate(plateCode: String): ResultResponse = ResultResponse(null, true)
        override suspend fun createAlarm(plateCode: String): ResultResponse = ResultResponse(null, true)
        override suspend fun removeAlarm(plateCode: String): ResultResponse = ResultResponse(null, true)
        override suspend fun createRemovalRequest(
            plateId: Long,
            request: AddPlateRemovalRequestRequest
        ): DataResultResponse<PlateRemovalRequestDto> =
            throw UnsupportedOperationException("not used in tests")
    }

    private class FakeAlarmPlateRepository : AlarmPlateRepository {
        private val alarmPlates = MutableStateFlow<List<AlarmPlate>>(emptyList())
        private val alarmCodes = MutableStateFlow<Set<String>>(emptySet())

        override fun observeAlarmPlates(): Flow<List<AlarmPlate>> = alarmPlates.asStateFlow()

        override fun observeAlarmPlateCodes(): Flow<Set<String>> = alarmCodes.asStateFlow()

        override suspend fun toggleAlarm(plate: AlarmPlate): AppResult<Boolean> {
            val code = plate.normalizedPlateCode
            return if (alarmCodes.value.contains(code)) {
                alarmCodes.value = alarmCodes.value - code
                alarmPlates.value = alarmPlates.value.filterNot { it.normalizedPlateCode == code }
                AppResult.Success(false)
            } else {
                alarmCodes.value = alarmCodes.value + code
                alarmPlates.value = listOf(plate) + alarmPlates.value
                AppResult.Success(true)
            }
        }

        override suspend fun replaceFromRemote(plates: List<AlarmPlate>) {
            alarmPlates.value = plates
            alarmCodes.value = plates.map { it.normalizedPlateCode }.toSet()
        }
    }

    private class FakeSavedPlateRepository : SavedPlateRepository {
        private val savedPlates = MutableStateFlow<List<SavedPlate>>(emptyList())
        private val savedCodes = MutableStateFlow<Set<String>>(emptySet())

        override fun observeSavedPlates(): Flow<List<SavedPlate>> = savedPlates.asStateFlow()

        override fun observeSavedPlateCodes(): Flow<Set<String>> = savedCodes.asStateFlow()

        override suspend fun replaceFromRemote(plates: List<SavedPlate>) {
            savedPlates.value = plates
            savedCodes.value = plates.map { it.normalizedPlateCode }.toSet()
        }

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
                id = 1L,
                plateCode = "34ABC123",
                cityName = "Istanbul",
                ratingAverage = 4.5,
                reviewCount = 15L,
                ratingDistribution = emptyList(),
                tagSummary = emptyList(),
                recentReviews = emptyList(),
                totalSearchCount = 5L,
                totalReviewCount = 2L,
                totalReportCount = 1L,
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
        )
    ) : PlateRepository {
        var callCount: Int = 0
        var lastPlateCode: String? = null

        override suspend fun searchPlate(plateCode: String): AppResult<PlateSearchResult> {
            callCount++
            lastPlateCode = plateCode
            return result
        }

        override suspend fun followPlate(plateCode: String): AppResult<Unit> = AppResult.Success(Unit)

        override suspend fun unfollowPlate(plateCode: String): AppResult<Unit> = AppResult.Success(Unit)

        override suspend fun createRemovalRequest(
            plateId: Long,
            reasonCode: String,
            description: String
        ): AppResult<Unit> = AppResult.Success(Unit)
    }
}

