package com.mefy.platemate.presentation.features.main.platedetail.actions

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.usecase.alarm.ObserveAlarmPlateCodesUseCase
import com.mefy.platemate.domain.usecase.alarm.ToggleAlarmPlateUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.PlateActionsDestination
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

@Immutable
data class PlateActionsUiState(
    val isLoading: Boolean = true,
    val plateCode: String = "",
    val cityCode: String = "",
    val isSaved: Boolean = false,
    val isAlarmed: Boolean = false,
    val isAlarmInProgress: Boolean = false
)

sealed interface PlateActionsUiAction {
    data object BackClicked : PlateActionsUiAction
    data object SaveToggled : PlateActionsUiAction
    data object AlarmToggled : PlateActionsUiAction
    data object ReportClicked : PlateActionsUiAction
}

sealed interface PlateActionsUiEffect {
    data object NavigateBack : PlateActionsUiEffect
    data class NavigateToRemoval(val plateId: Long, val plateCode: String) : PlateActionsUiEffect
}

@HiltViewModel
class PlateActionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchPlateUseCase: SearchPlateUseCase,
    private val observeSavedPlateCodesUseCase: ObserveSavedPlateCodesUseCase,
    private val toggleSavedPlateUseCase: ToggleSavedPlateUseCase,
    private val observeAlarmPlateCodesUseCase: ObserveAlarmPlateCodesUseCase,
    private val toggleAlarmPlateUseCase: ToggleAlarmPlateUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val normalizedPlateCode: String = savedStateHandle.toRoute<PlateActionsDestination>().plateCode

    private val _uiState = MutableStateFlow(PlateActionsUiState())
    val uiState: StateFlow<PlateActionsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PlateActionsUiEffect>()
    val uiEffect: SharedFlow<PlateActionsUiEffect> = _uiEffect.asSharedFlow()

    private var latestPlate: PlateSearchResult? = null

    init {
        loadPlate()
        observeSaved()
        observeAlarm()
    }

    fun onAction(action: PlateActionsUiAction) {
        when (action) {
            PlateActionsUiAction.BackClicked -> launch { _uiEffect.emit(PlateActionsUiEffect.NavigateBack) }
            PlateActionsUiAction.SaveToggled -> onSaveToggled()
            PlateActionsUiAction.AlarmToggled -> onAlarmToggled()
            PlateActionsUiAction.ReportClicked -> onReportClicked()
        }
    }

    private fun loadPlate() {
        launch {
            when (val result = searchPlateUseCase(normalizedPlateCode)) {
                is AppResult.Success -> {
                    latestPlate = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            plateCode = formatTurkishPlateInputUseCase(result.data.plateCode),
                            cityCode = result.data.plateCode.take(2)
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    handleError(result.error)
                }
            }
        }
    }

    private fun observeSaved() {
        launch {
            observeSavedPlateCodesUseCase().collectLatest { codes ->
                _uiState.update { it.copy(isSaved = codes.contains(normalizedPlateCode)) }
            }
        }
    }

    private fun observeAlarm() {
        launch {
            observeAlarmPlateCodesUseCase().collectLatest { codes ->
                _uiState.update { it.copy(isAlarmed = codes.contains(normalizedPlateCode)) }
            }
        }
    }

    private fun onSaveToggled() {
        val plate = latestPlate ?: return
        launch {
            toggleSavedPlateUseCase(
                SavedPlate(
                    normalizedPlateCode = normalizedPlateCode,
                    formattedPlateCode = formatTurkishPlateInputUseCase(plate.plateCode),
                    cityName = CityNameResolver.resolveCityName(plate.cityName, plate.plateCode),
                    ratingAverage = plate.ratingAverage,
                    commentCount = plate.reviewCount,
                    reportTypes = plate.topReportTypes,
                    savedAt = System.currentTimeMillis()
                )
            )
        }
    }

    private fun onAlarmToggled() {
        val plate = latestPlate ?: return
        if (_uiState.value.isAlarmInProgress) return

        _uiState.update { it.copy(isAlarmInProgress = true) }
        launch {
            val result = toggleAlarmPlateUseCase(
                AlarmPlate(
                    normalizedPlateCode = normalizedPlateCode,
                    formattedPlateCode = formatTurkishPlateInputUseCase(plate.plateCode),
                    cityName = CityNameResolver.resolveCityName(plate.cityName, plate.plateCode),
                    ratingAverage = plate.ratingAverage,
                    commentCount = plate.reviewCount,
                    reportTypes = plate.topReportTypes,
                    savedAt = System.currentTimeMillis()
                )
            )
            _uiState.update { it.copy(isAlarmInProgress = false) }
            if (result is AppResult.Error) showError(result.error.toUiText())
        }
    }

    private fun onReportClicked() {
        val plate = latestPlate ?: return
        launch { _uiEffect.emit(PlateActionsUiEffect.NavigateToRemoval(plate.id, normalizedPlateCode)) }
    }
}
