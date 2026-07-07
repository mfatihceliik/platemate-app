package com.mefy.platemate.presentation.features.admin.reporttypes

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.ReportTypeAdmin
import com.mefy.platemate.domain.usecase.admin.GetReportTypesUseCase
import com.mefy.platemate.domain.usecase.admin.SetReportTypeActiveUseCase
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
class ReportTypesViewModel @Inject constructor(
    private val getReportTypesUseCase: GetReportTypesUseCase,
    private val setReportTypeActiveUseCase: SetReportTypeActiveUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(ReportTypesUiState())
    val uiState: StateFlow<ReportTypesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ReportTypesUiEffect>()
    val uiEffect: SharedFlow<ReportTypesUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: ReportTypesUiAction) {
        when (action) {
            ReportTypesUiAction.BackClicked -> _uiEffect.emitUiEffect(ReportTypesUiEffect.NavigateBack)
            ReportTypesUiAction.RetryClicked -> refresh()
            ReportTypesUiAction.AddClicked -> _uiEffect.emitUiEffect(ReportTypesUiEffect.NavigateToForm(null))
            is ReportTypesUiAction.EditClicked -> _uiEffect.emitUiEffect(ReportTypesUiEffect.NavigateToForm(action.id))
            is ReportTypesUiAction.ActiveToggled -> toggleActive(action.id, action.active)
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = it.items.isEmpty(), errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getReportTypesUseCase()) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, items = result.data.map(ReportTypeAdmin::toListItem)) }
                is AppResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
            }
        }
    }

    private fun toggleActive(id: Long, currentActive: Boolean) {
        if (_uiState.value.togglingId != null) return
        val target = !currentActive
        _uiState.update { state ->
            state.copy(
                togglingId = id,
                items = state.items.map { if (it.id == id) it.copy(active = target) else it }
            )
        }
        launch(onError = { error ->
            revertToggle(id, currentActive)
            handleError(error)
        }) {
            val result = setReportTypeActiveUseCase(id, target)
            if (result is AppResult.Error) {
                revertToggle(id, currentActive)
                showError(result.error.toUiText())
            } else {
                _uiState.update { it.copy(togglingId = null) }
            }
        }
    }

    private fun revertToggle(id: Long, original: Boolean) {
        _uiState.update { state ->
            state.copy(
                togglingId = null,
                items = state.items.map { if (it.id == id) it.copy(active = original) else it }
            )
        }
    }
}

private fun ReportTypeAdmin.toListItem(): ReportTypeListItem = ReportTypeListItem(
    id = id,
    code = code,
    label = label,
    severityCode = severityCode,
    active = active
)
