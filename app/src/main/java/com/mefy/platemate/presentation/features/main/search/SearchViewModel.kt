package com.mefy.platemate.presentation.features.main.search

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.usecase.alarm.ObserveAlarmPlatesUseCase
import com.mefy.platemate.domain.usecase.alarm.SyncMyPlateListsUseCase
import com.mefy.platemate.domain.usecase.alarm.ToggleAlarmPlateUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.ClearRecentSearchesUseCase
import com.mefy.platemate.domain.usecase.search.DeleteRecentSearchUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ObserveRecentSearchesUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlatesUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.domain.usecase.search.UpsertRecentSearchUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.search.mapper.SearchUiMapper
import com.mefy.platemate.presentation.features.main.search.reducer.SearchStateReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlateUseCase: SearchPlateUseCase,
    private val observeRecentSearchesUseCase: ObserveRecentSearchesUseCase,
    private val observeSavedPlatesUseCase: ObserveSavedPlatesUseCase,
    private val observeSavedPlateCodesUseCase: ObserveSavedPlateCodesUseCase,
    private val observeAlarmPlatesUseCase: ObserveAlarmPlatesUseCase,
    private val toggleAlarmPlateUseCase: ToggleAlarmPlateUseCase,
    private val syncMyPlateListsUseCase: SyncMyPlateListsUseCase,
    private val upsertRecentSearchUseCase: UpsertRecentSearchUseCase,
    private val toggleSavedPlateUseCase: ToggleSavedPlateUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
    private val clearRecentSearchesUseCase: ClearRecentSearchesUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase,
    private val searchUiMapper: SearchUiMapper,
    private val searchStateReducer: SearchStateReducer,
    private val appDispatchers: AppDispatchers,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SearchUiEffect>()
    val uiEffect: SharedFlow<SearchUiEffect> = _uiEffect.asSharedFlow()
    // Mapping arka planda (flowOn Default) yapıldığından, Main'den okunan bu
    // cache'ler @Volatile (görünürlük garantisi).
    @Suppress("unused")
    @Volatile
    private var latestRecentItems: List<RecentSearch> = emptyList()
    @Suppress("unused")
    @Volatile
    private var latestSavedPlates: List<com.mefy.platemate.domain.model.search.SavedPlate> = emptyList()
    @Suppress("unused")
    @Volatile
    private var latestAlarmPlates: List<AlarmPlate> = emptyList()

    init {
        observeAllData()
        syncMyLists()
    }

    /** SearchScreen açılışında tek istek: saved + alarm listelerini backend'den çekip cache'i yeniler. */
    private fun syncMyLists() {
        launch { syncMyPlateListsUseCase() }
    }

    // Removed observeAlarmPlates()

    private fun observeAllData() {
        launch(
            onError = { throwable -> handleError(throwable) }
        ) {
            combine(
                observeRecentSearchesUseCase(),
                observeSavedPlateCodesUseCase(),
                observeSavedPlatesUseCase(),
                observeAlarmPlatesUseCase()
            ) { recentItems, bookmarkedCodes, savedPlates, alarms ->
                latestRecentItems = recentItems
                latestSavedPlates = savedPlates
                latestAlarmPlates = alarms
                
                val mappedRecent = searchUiMapper.mapRecentSearches(recentItems, bookmarkedCodes)
                val mappedSaved = searchUiMapper.mapSavedPlates(savedPlates)
                val mappedAlarms = searchUiMapper.mapAlarmPlates(alarms)
                
                Triple(mappedRecent, mappedSaved, mappedAlarms)
            }.flowOn(appDispatchers.default).collectLatest { (mappedRecent, mappedSaved, mappedAlarms) ->
                _uiState.update { current ->
                    searchStateReducer.onDataUpdated(current, mappedRecent, mappedSaved, mappedAlarms)
                }
            }
        }
    }

    fun onAction(action: SearchUiAction) {
        when (action) {
            is SearchUiAction.PlateInputChanged -> onPlateInputChanged(action.value)
            SearchUiAction.SearchClicked -> onSearchClicked()
            is SearchUiAction.RecentItemClicked -> onRecentItemClicked(action.plateCode)
            is SearchUiAction.RecentBookmarkClicked -> onRecentBookmarkClicked(action.normalizedPlateCode)
            is SearchUiAction.SavedPlateBookmarkClicked -> onSavedPlateBookmarkClicked(action.normalizedPlateCode)
            is SearchUiAction.AlarmPlateRemoveClicked -> onAlarmPlateRemoveClicked(action.normalizedPlateCode)
            is SearchUiAction.RecentDismissClicked -> onRecentDismissClicked(action.normalizedPlateCode)
            SearchUiAction.ClearRecentClicked -> onClearRecentClicked()
            SearchUiAction.RetryClicked -> syncMyLists()
        }
    }

    private fun onPlateInputChanged(value: String) {
        val formattedPlate = formatTurkishPlateInputUseCase(value)
        val compactPlate = compactPlateCode(formattedPlate)
        val cityName = searchUiMapper.detectCityFromPlate(compactPlate)
        val isPlateValid = validateTurkishPlateUseCase(formattedPlate)

        _uiState.update { current ->
            searchStateReducer.onPlateInputChanged(
                state = current,
                formattedPlate = formattedPlate,
                isPlateValid = isPlateValid,
                detectedCityName = cityName
            )
        }
    }

    private fun onSearchClicked() {
        val currentState = _uiState.value
        if (currentState.isSearching) return
        if (!currentState.isPlateValid) return

        val normalizedPlate = validateTurkishPlateUseCase.normalize(currentState.plateInput)

        _uiState.update { current ->
            searchStateReducer.onSearchLoading(current)
        }

        launch {
            when (val result = searchPlateUseCase(normalizedPlate)) {
                is AppResult.Success -> onSearchSuccess(result.data, normalizedPlate)
                is AppResult.Error -> onSearchError(result.error)
            }
        }
    }

    private suspend fun onSearchSuccess(
        result: PlateSearchResult,
        normalizedPlate: String
    ) {
        val formattedPlate = formatTurkishPlateInputUseCase(result.plateCode)
        val detectedCity = searchUiMapper.resolveCityName(result.cityName, normalizedPlate)
        val recentItem = searchUiMapper.mapRecentSearch(
            result = result,
            normalizedPlate = normalizedPlate,
            formattedPlate = formattedPlate,
            detectedCityName = detectedCity
        )
        upsertRecentSearchUseCase(recentItem)

        _uiState.update { current ->
            searchStateReducer.onSearchSuccess(
                state = current,
                formattedPlate = formattedPlate,
                detectedCityName = detectedCity
            )
        }

        _uiEffect.emitUiEffect(SearchUiEffect.NavigateToSearchDetail(id = normalizedPlate))
    }

    private fun onSearchError(error: AppError) {
        // Hata ortak kanaldan: Server -> snackbar, bağlantı -> pop-up. Ekran aksiyonu Idle'a çeker.
        handleError(error)
        _uiState.update { current ->
            searchStateReducer.onSearchError(current)
        }
    }

    private fun onRecentItemClicked(plateCode: String) {
        onPlateInputChanged(plateCode)
    }

    private fun onRecentBookmarkClicked(normalizedPlateCode: String) {
        launch {
            val recentItem = latestRecentItems.firstOrNull {
                it.normalizedPlateCode == normalizedPlateCode
            } ?: return@launch

            toggleSavedPlateUseCase(searchUiMapper.mapSavedPlate(recentItem))
        }
    }

    private fun onSavedPlateBookmarkClicked(normalizedPlateCode: String) {
        launch {
            val savedPlate = latestSavedPlates.firstOrNull {
                it.normalizedPlateCode == normalizedPlateCode
            } ?: return@launch

            toggleSavedPlateUseCase(savedPlate)
        }
    }

    private fun onAlarmPlateRemoveClicked(normalizedPlateCode: String) {
        launch {
            val alarmPlate = latestAlarmPlates.firstOrNull {
                it.normalizedPlateCode == normalizedPlateCode
            } ?: return@launch

            when (val result = toggleAlarmPlateUseCase(alarmPlate)) {
                is AppResult.Error -> handleError(result.error)
                is AppResult.Success -> Unit
            }
        }
    }

    private fun onClearRecentClicked() {
        launch {
            clearRecentSearchesUseCase()
        }
    }

    private fun onRecentDismissClicked(normalizedPlateCode: String) {
        launch {
            deleteRecentSearchUseCase(normalizedPlateCode)
        }
    }

    private fun compactPlateCode(input: String): String = validateTurkishPlateUseCase.normalize(input)
}
