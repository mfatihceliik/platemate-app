package com.mefy.platemate.presentation.features.admin.reporttypes.form

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.ReportTypeInput
import com.mefy.platemate.domain.usecase.admin.GetReportTypesUseCase
import com.mefy.platemate.domain.usecase.admin.SaveReportTypeUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.AdminReportTypeFormDestination
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
class ReportTypeFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getReportTypesUseCase: GetReportTypesUseCase,
    private val saveReportTypeUseCase: SaveReportTypeUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: AdminReportTypeFormDestination = savedStateHandle.toRoute()
    private val typeId: Long? = route.typeId.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(ReportTypeFormUiState(isEdit = typeId != null))
    val uiState: StateFlow<ReportTypeFormUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ReportTypeFormUiEffect>()
    val uiEffect: SharedFlow<ReportTypeFormUiEffect> = _uiEffect.asSharedFlow()

    init {
        if (typeId != null) prefill(typeId)
    }

    fun onAction(action: ReportTypeFormUiAction) {
        when (action) {
            ReportTypeFormUiAction.BackClicked -> _uiEffect.emitUiEffect(ReportTypeFormUiEffect.NavigateBack)
            ReportTypeFormUiAction.SaveClicked -> save()
            is ReportTypeFormUiAction.CodeChanged -> _uiState.update { it.copy(code = action.value) }
            is ReportTypeFormUiAction.LabelChanged -> _uiState.update { it.copy(label = action.value) }
            is ReportTypeFormUiAction.DescriptionChanged -> _uiState.update { it.copy(description = action.value) }
            is ReportTypeFormUiAction.IconKeyChanged -> _uiState.update { it.copy(iconKey = action.value) }
            is ReportTypeFormUiAction.SeverityChanged -> _uiState.update { it.copy(severityCode = action.value) }
            is ReportTypeFormUiAction.ColorHexChanged -> _uiState.update { it.copy(colorHex = action.value) }
            is ReportTypeFormUiAction.WeightChanged -> _uiState.update { it.copy(weight = action.value.filter(Char::isDigit).take(4)) }
            is ReportTypeFormUiAction.SortOrderChanged -> _uiState.update { it.copy(sortOrder = action.value.filter(Char::isDigit).take(4)) }
        }
    }

    private fun prefill(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false) }
            handleError(error)
        }) {
            when (val result = getReportTypesUseCase()) {
                is AppResult.Success -> {
                    val type = result.data.firstOrNull { it.id == id }
                    if (type == null) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            code = type.code,
                            label = type.label,
                            description = type.description,
                            iconKey = type.iconKey,
                            severityCode = type.severityCode.ifBlank { "RED" },
                            colorHex = type.colorHex.ifBlank { "#" },
                            weight = type.weight.toString(),
                            sortOrder = type.sortOrder.toString()
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        if (!state.isSaveEnabled) return
        val input = ReportTypeInput(
            code = state.code.trim(),
            label = state.label.trim(),
            description = state.description.trim(),
            iconKey = state.iconKey.trim(),
            severityCode = state.severityCode,
            colorHex = state.colorHex.trim(),
            weight = state.weight.toInt(),
            sortOrder = state.sortOrder.toInt()
        )
        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            when (val result = saveReportTypeUseCase(typeId, input)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.admin_report_type_saved))
                    _uiEffect.emit(ReportTypeFormUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}
