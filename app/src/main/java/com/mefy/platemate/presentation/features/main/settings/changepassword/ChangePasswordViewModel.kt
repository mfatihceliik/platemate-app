package com.mefy.platemate.presentation.features.main.settings.changepassword

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.auth.ChangePasswordUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
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
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ChangePasswordUiEffect>()
    val uiEffect: SharedFlow<ChangePasswordUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: ChangePasswordUiAction) {
        when (action) {
            is ChangePasswordUiAction.CurrentPasswordChanged ->
                _uiState.update { it.copy(currentPassword = action.value) }

            is ChangePasswordUiAction.NewPasswordChanged ->
                _uiState.update { it.copy(newPassword = action.value) }

            is ChangePasswordUiAction.ConfirmPasswordChanged ->
                _uiState.update { it.copy(confirmPassword = action.value) }

            ChangePasswordUiAction.SubmitClicked -> submitPasswordChange()
            ChangePasswordUiAction.BackClicked ->
                _uiEffect.emitUiEffect(ChangePasswordUiEffect.NavigateBack)
        }
    }

    private fun submitPasswordChange() {
        val state = _uiState.value
        if (!state.isSubmitEnabled) return

        _uiState.update { it.copy(isSubmitting = true) }
        launch(
            onError = {
                _uiState.update { current -> current.copy(isSubmitting = false) }
                handleError(it)
            }
        ) {
            when (val result = changePasswordUseCase(state.currentPassword, state.newPassword)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    _uiEffect.emitUiEffect(ChangePasswordUiEffect.PasswordChanged)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}
