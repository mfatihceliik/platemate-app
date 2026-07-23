package com.mefy.platemate.presentation.features.main.discover.cityplates

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.discovery.GetDiscoveryCityPlatesUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.discover.cityplates.mapper.CityPlatesUiMapper
import com.mefy.platemate.presentation.navigation.DiscoverCityPlatesDestination
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
class CityPlatesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDiscoveryCityPlatesUseCase: GetDiscoveryCityPlatesUseCase,
    private val cityPlatesUiMapper: CityPlatesUiMapper,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: DiscoverCityPlatesDestination = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(CityPlatesUiState(cityName = route.cityName))
    val uiState: StateFlow<CityPlatesUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<CityPlatesUiEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEffects: SharedFlow<CityPlatesUiEffect> = _uiEffects.asSharedFlow()

    init {
        loadFirstPage()
    }

    fun onAction(action: CityPlatesUiAction) {
        when (action) {
            is CityPlatesUiAction.PlateClicked -> onPlateClicked(action.plateId)
            CityPlatesUiAction.LoadMoreRequested -> loadNextPage()
            CityPlatesUiAction.BackClicked -> emitEffect(CityPlatesUiEffect.NavigateBack)
        }
    }

    override fun onRetry() {
        loadFirstPage()
    }

    private fun loadFirstPage() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch {
            when (val result = getDiscoveryCityPlatesUseCase(cityId = route.cityId, page = 0)) {
                is AppResult.Success -> _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = null,
                        plates = cityPlatesUiMapper.mapPlates(result.data.items, startRank = 1),
                        page = 0,
                        endReached = !result.data.hasNext
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toUiText())
                }
            }
        }
    }

    private fun loadNextPage() {
        val current = _uiState.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return

        _uiState.update { it.copy(isLoadingMore = true) }
        launch {
            val nextPage = current.page + 1
            when (val result = getDiscoveryCityPlatesUseCase(cityId = route.cityId, page = nextPage)) {
                is AppResult.Success -> _uiState.update { state ->
                    state.copy(
                        isLoadingMore = false,
                        plates = state.plates + cityPlatesUiMapper.mapPlates(
                            result.data.items,
                            startRank = state.plates.size + 1
                        ),
                        page = nextPage,
                        endReached = !result.data.hasNext
                    )
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun onPlateClicked(plateId: String) {
        val normalizedCode = validateTurkishPlateUseCase.normalize(plateId)
        emitEffect(CityPlatesUiEffect.NavigateToPlateDetail(normalizedCode))
    }

    private fun emitEffect(effect: CityPlatesUiEffect) {
        launch { _uiEffects.emit(effect) }
    }
}
