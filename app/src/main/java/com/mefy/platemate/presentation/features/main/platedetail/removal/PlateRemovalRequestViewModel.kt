package com.mefy.platemate.presentation.features.main.platedetail.removal

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.plate.CreatePlateRemovalRequestUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.platedetail.removal.model.PlateRemovalReason
import com.mefy.platemate.presentation.navigation.PlateRemovalRequestDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Immutable
data class PlateRemovalRequestUiState(
    val plateCode: String = "",
    val selectedReason: PlateRemovalReason? = null,
    val description: String = "",
    val isSubmitting: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = selectedReason != null && description.isNotBlank() && !isSubmitting

    companion object {
        const val DESCRIPTION_MAX_LENGTH = 1000
    }
}

sealed interface PlateRemovalRequestUiAction {
    data object BackClicked : PlateRemovalRequestUiAction
    data class ReasonSelected(val reason: PlateRemovalReason) : PlateRemovalRequestUiAction
    data class DescriptionChanged(val value: String) : PlateRemovalRequestUiAction
    data object SubmitClicked : PlateRemovalRequestUiAction
}

sealed interface PlateRemovalRequestUiEffect {
    data object NavigateBack : PlateRemovalRequestUiEffect
}

@HiltViewModel
class PlateRemovalRequestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createPlateRemovalRequestUseCase: CreatePlateRemovalRequestUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: PlateRemovalRequestDestination = savedStateHandle.toRoute()
    private val plateId: Long = route.plateId

    private val _uiState = MutableStateFlow(PlateRemovalRequestUiState(plateCode = route.plateCode))
    val uiState: StateFlow<PlateRemovalRequestUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PlateRemovalRequestUiEffect>()
    val uiEffect: SharedFlow<PlateRemovalRequestUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: PlateRemovalRequestUiAction) {
        when (action) {
            PlateRemovalRequestUiAction.BackClicked -> _uiEffect.emitUiEffect(PlateRemovalRequestUiEffect.NavigateBack)
            is PlateRemovalRequestUiAction.ReasonSelected -> _uiState.update { it.copy(selectedReason = action.reason) }
            is PlateRemovalRequestUiAction.DescriptionChanged -> {
                if (action.value.length <= PlateRemovalRequestUiState.DESCRIPTION_MAX_LENGTH) {
                    _uiState.update { it.copy(description = action.value) }
                }
            }
            PlateRemovalRequestUiAction.SubmitClicked -> submit()
        }
    }

    private fun submit() {
        val state = _uiState.value
        val reason = state.selectedReason ?: return
        if (!state.isSubmitEnabled) return

        _uiState.update { it.copy(isSubmitting = true) }
        launch {
            when (val result = createPlateRemovalRequestUseCase(plateId, reason.code, state.description)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    showSuccess(UiText.Resource(R.string.removal_request_success))
                    _uiEffect.emit(PlateRemovalRequestUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}
