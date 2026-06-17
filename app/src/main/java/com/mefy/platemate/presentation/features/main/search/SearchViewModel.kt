package com.mefy.platemate.presentation.features.main.search

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.search.RecentSearch
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
import com.mefy.platemate.presentation.common.error.ErrorContext
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import com.mefy.platemate.presentation.common.state.UiActionState
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
import kotlinx.coroutines.flow.update

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlateUseCase: SearchPlateUseCase,
    private val observeRecentSearchesUseCase: ObserveRecentSearchesUseCase,
    private val observeSavedPlatesUseCase: ObserveSavedPlatesUseCase,
    private val observeSavedPlateCodesUseCase: ObserveSavedPlateCodesUseCase,
    private val upsertRecentSearchUseCase: UpsertRecentSearchUseCase,
    private val toggleSavedPlateUseCase: ToggleSavedPlateUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
    private val clearRecentSearchesUseCase: ClearRecentSearchesUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase,
    private val searchUiMapper: SearchUiMapper,
    private val searchStateReducer: SearchStateReducer,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val _uiState = MutableStateFlow(SearchUiState(isInitialLoading = true))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SearchUiEffect>()
    val uiEffect: SharedFlow<SearchUiEffect> = _uiEffect.asSharedFlow()
    private var latestRecentItems: List<RecentSearch> = emptyList()
    private var latestSavedPlates: List<com.mefy.platemate.domain.model.search.SavedPlate> = emptyList()

    init {
        observeRecentSearches()
    }

    private fun observeRecentSearches() {
        launch(
            onError = { throwable ->
                _uiState.update { current ->
                    searchStateReducer.onInitialLoadFailed(current)
                }
                handleError(throwable)
            }
        ) {
            combine(
                observeRecentSearchesUseCase(),
                observeSavedPlateCodesUseCase(),
                observeSavedPlatesUseCase()
            ) { recentItems, bookmarkedCodes, savedPlates ->
                latestRecentItems = recentItems
                latestSavedPlates = savedPlates
                val mappedRecent = searchUiMapper.mapRecentSearches(recentItems, bookmarkedCodes)
                val mappedSaved = searchUiMapper.mapSavedPlates(savedPlates)
                Pair(mappedRecent, mappedSaved)
            }.collectLatest { (mappedRecent, mappedSaved) ->
                _uiState.update { current ->
                    searchStateReducer.onDataUpdated(current, mappedRecent, mappedSaved)
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
            is SearchUiAction.RecentDismissClicked -> onRecentDismissClicked(action.normalizedPlateCode)
            SearchUiAction.ClearRecentClicked -> onClearRecentClicked()
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
        if (currentState.submitState is UiActionState.Loading) return
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
        val resolvedError = handleError(
            error = error,
            context = ErrorContext.Search,
            consumeUxAction = false
        )
        _uiState.update { current ->
            searchStateReducer.onSearchError(current, resolvedError.message)
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
