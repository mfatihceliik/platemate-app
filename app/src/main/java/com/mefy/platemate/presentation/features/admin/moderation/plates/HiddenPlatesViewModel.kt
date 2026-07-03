package com.mefy.platemate.presentation.features.admin.moderation.plates

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.HiddenPlate
import com.mefy.platemate.domain.usecase.admin.GetHiddenPlatesUseCase
import com.mefy.platemate.domain.usecase.admin.RestorePlateUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
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
class HiddenPlatesViewModel @Inject constructor(
    private val getHiddenPlatesUseCase: GetHiddenPlatesUseCase,
    private val restorePlateUseCase: RestorePlateUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(HiddenPlatesUiState())
    val uiState: StateFlow<HiddenPlatesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<HiddenPlatesUiEffect>()
    val uiEffect: SharedFlow<HiddenPlatesUiEffect> = _uiEffect.asSharedFlow()

    private var currentPage = 0
    private var hasNext = true

    init {
        loadFirstPage()
    }

    fun onAction(action: HiddenPlatesUiAction) {
        when (action) {
            HiddenPlatesUiAction.BackClicked -> _uiEffect.emitUiEffect(HiddenPlatesUiEffect.NavigateBack)
            HiddenPlatesUiAction.RetryClicked -> loadFirstPage()
            HiddenPlatesUiAction.LoadMore -> loadNextPage()
            is HiddenPlatesUiAction.RestoreClicked -> restore(action.plateId)
        }
    }

    private fun loadFirstPage() {
        currentPage = 0
        hasNext = true
        _uiState.update { it.copy(isLoading = true, errorMessage = null, items = emptyList()) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getHiddenPlatesUseCase(currentPage)) {
                is AppResult.Success -> {
                    hasNext = result.data.meta.hasNext
                    _uiState.update { it.copy(isLoading = false, items = result.data.items.map(HiddenPlate::toUiModel)) }
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
            }
        }
    }

    private fun loadNextPage() {
        if (_uiState.value.isLoadingMore || !hasNext) return
        _uiState.update { it.copy(isLoadingMore = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoadingMore = false) }
            handleError(error)
        }) {
            when (val result = getHiddenPlatesUseCase(currentPage + 1)) {
                is AppResult.Success -> {
                    currentPage += 1
                    hasNext = result.data.meta.hasNext
                    _uiState.update { it.copy(isLoadingMore = false, items = it.items + result.data.items.map(HiddenPlate::toUiModel)) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun restore(plateId: Long) {
        if (_uiState.value.actioningId != null) return
        _uiState.update { it.copy(actioningId = plateId) }
        launch(onError = { error ->
            _uiState.update { it.copy(actioningId = null) }
            handleError(error)
        }) {
            when (val result = restorePlateUseCase(plateId)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(actioningId = null, items = it.items.filterNot { item -> item.id == plateId }) }
                    showSuccess(UiText.Resource(R.string.admin_plate_restored))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(actioningId = null) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}

private fun HiddenPlate.toUiModel(): HiddenPlateUiModel = HiddenPlateUiModel(
    id = id,
    plateCode = plateCode,
    statusCode = statusCode,
    hiddenReason = hiddenReason,
    reportCount = reportCount
)
