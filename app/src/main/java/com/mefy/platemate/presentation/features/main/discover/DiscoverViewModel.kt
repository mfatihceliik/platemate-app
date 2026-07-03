package com.mefy.platemate.presentation.features.main.discover

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.usecase.discovery.GetDiscoveryHomeUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.discover.mapper.DiscoverUiMapper
import com.mefy.platemate.presentation.features.main.discover.reducer.DiscoverStateReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getDiscoveryHomeUseCase: GetDiscoveryHomeUseCase,
    private val observeSavedPlateCodesUseCase: ObserveSavedPlateCodesUseCase,
    private val toggleSavedPlateUseCase: ToggleSavedPlateUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase,
    private val discoverUiMapper: DiscoverUiMapper,
    private val discoverStateReducer: DiscoverStateReducer,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<DiscoverUiEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEffects: SharedFlow<DiscoverUiEffect> = _uiEffects.asSharedFlow()

    private var currentTabs: DiscoveryTabs? = null
    private var latestBookmarkedCodes: Set<String> = emptySet()

    init {
        observeBookmarks()
        loadDiscoveryHome(forceRefresh = false)
    }

    private fun observeBookmarks() {
        launch {
            observeSavedPlateCodesUseCase().collectLatest { codes ->
                latestBookmarkedCodes = codes
                if (currentTabs != null) {
                    val plateDetails = discoverUiMapper.mapTabPlates(
                        tabs = currentTabs!!,
                        filter = _uiState.value.selectedFilter,
                        bookmarkedCodes = codes
                    )
                    _uiState.update { current ->
                        discoverStateReducer.onFilterSelected(
                            state = current,
                            filter = current.selectedFilter,
                            plateDetails = plateDetails
                        )
                    }
                }
            }
        }
    }

    private fun loadDiscoveryHome(forceRefresh: Boolean) {
        val refreshing = shouldShowRefreshingState(current = _uiState.value, forceRefresh = forceRefresh)
        _uiState.update { current ->
            if (refreshing) {
                discoverStateReducer.onRefreshing(current)
            } else {
                discoverStateReducer.onInitialLoading(current)
            }
        }
        launch {
            when (val result = getDiscoveryHomeUseCase(forceRefresh = forceRefresh)) {
                is AppResult.Success -> {
                    val mappedHome = discoverUiMapper.mapHome(result.data)
                    currentTabs = mappedHome.tabs
                    val plateDetails = discoverUiMapper.mapTabPlates(
                        tabs = mappedHome.tabs,
                        filter = _uiState.value.selectedFilter,
                        bookmarkedCodes = latestBookmarkedCodes
                    )
                    _uiState.update { current ->
                        discoverStateReducer.onContentLoaded(
                            state = current,
                            mappedHome = mappedHome,
                            plateDetails = plateDetails
                        )
                    }
                }
                is AppResult.Error -> {
                    val message = result.error.toUiText()
                    if (refreshing) {
                        // İçerik zaten var; yenileme hatası snackbar ile bildirilir.
                        _uiState.update { it.copy(isRefreshing = false) }
                        showError(message)
                    } else {
                        _uiState.update { discoverStateReducer.onLoadError(it, message) }
                    }
                }
            }
        }
    }

    fun onAction(action: DiscoverUiAction) {
        when (action) {
            is DiscoverUiAction.FilterSelected -> onFilterSelected(action.filter)
            is DiscoverUiAction.TrendPlateClicked -> onTrendPlateClicked(action.trendId)
            is DiscoverUiAction.TrendPlateBookmarkClicked -> onTrendPlateBookmarkClicked(action.trendId)
            DiscoverUiAction.RefreshRequested -> onRefreshRequested()
            DiscoverUiAction.RetryClicked -> loadDiscoveryHome(forceRefresh = false)
        }
    }

    private fun onFilterSelected(filter: DiscoverFilterUi) {
        val tabs = currentTabs ?: return
        val plateDetails = discoverUiMapper.mapTabPlates(tabs = tabs, filter = filter, bookmarkedCodes = latestBookmarkedCodes)
        _uiState.update { current ->
            discoverStateReducer.onFilterSelected(
                state = current,
                filter = filter,
                plateDetails = plateDetails
            )
        }
    }

    private fun onTrendPlateClicked(trendId: String) {
        val plateCode = trendId.substringBefore("_")
        val normalizedCode = validateTurkishPlateUseCase.normalize(plateCode)
        launch {
            _uiEffects.emit(DiscoverUiEffect.NavigateToTrendDetail(normalizedCode))
        }
    }

    private fun onTrendPlateBookmarkClicked(trendId: String) {
        launch {
            val tabs = currentTabs ?: return@launch
            val plateCode = trendId.substringBefore("_")
            val allPlates = tabs.trendPlates + tabs.attentionPlates + tabs.goodDriverPlates + tabs.newPlates
            val plateDetail = allPlates.firstOrNull { it.plateCode == plateCode } ?: return@launch
            
            val formattedCode = formatTurkishPlateInputUseCase(plateDetail.plateCode)
            val normalizedCode = validateTurkishPlateUseCase.normalize(plateDetail.plateCode)
            
            val savedPlate = com.mefy.platemate.domain.model.search.SavedPlate(
                normalizedPlateCode = normalizedCode,
                formattedPlateCode = formattedCode,
                cityName = com.mefy.platemate.presentation.common.text.CityNameResolver.resolveCityName(
                    cityName = plateDetail.cityName,
                    plateCode = plateDetail.plateCode
                ),
                ratingAverage = plateDetail.ratingAverage,
                commentCount = plateDetail.reviewCount,
                reportTypes = plateDetail.topReportType,
                savedAt = System.currentTimeMillis()
            )
            
            toggleSavedPlateUseCase(savedPlate)
        }
    }

    private fun onRefreshRequested() {
        val state = _uiState.value
        if (state.isInitialLoading || state.isRefreshing) return
        loadDiscoveryHome(forceRefresh = true)
    }

    private fun shouldShowRefreshingState(
        current: DiscoverUiState,
        forceRefresh: Boolean
    ): Boolean {
        if (!forceRefresh) return false
        return currentTabs != null || current.metrics.isNotEmpty() || current.plateDetail.isNotEmpty()
    }

}
