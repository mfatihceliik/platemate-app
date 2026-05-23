package com.mefy.platemate.presentation.features.main.discover

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.usecase.discovery.GetDiscoveryHomeUseCase
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
import kotlinx.coroutines.flow.update

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getDiscoveryHomeUseCase: GetDiscoveryHomeUseCase,
    private val discoverUiMapper: DiscoverUiMapper,
    private val discoverStateReducer: DiscoverStateReducer
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<DiscoverUiEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEffects: SharedFlow<DiscoverUiEffect> = _uiEffects.asSharedFlow()

    private var currentTabs: DiscoveryTabs? = null

    init {
        loadDiscoveryHome(forceRefresh = false)
    }

    private fun loadDiscoveryHome(forceRefresh: Boolean) {
        _uiState.update { current ->
            if (shouldShowRefreshingState(current = current, forceRefresh = forceRefresh)) {
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
                        filter = _uiState.value.selectedFilter
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
                    _uiState.update { discoverStateReducer.onLoadError(it) }
                }
            }
        }
    }

    fun onAction(action: DiscoverUiAction) {
        when (action) {
            is DiscoverUiAction.FilterSelected -> onFilterSelected(action.filter)
            is DiscoverUiAction.TrendPlateClicked -> onTrendPlateClicked(action.trendId)
            DiscoverUiAction.RefreshRequested -> onRefreshRequested()
        }
    }

    private fun onFilterSelected(filter: DiscoverFilterUi) {
        val tabs = currentTabs ?: return
        val plateDetails = discoverUiMapper.mapTabPlates(tabs = tabs, filter = filter)
        _uiState.update { current ->
            discoverStateReducer.onFilterSelected(
                state = current,
                filter = filter,
                plateDetails = plateDetails
            )
        }
    }

    private fun onTrendPlateClicked(trendId: String) {
        launch {
            _uiEffects.emit(DiscoverUiEffect.NavigateToTrendDetail(trendId))
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
