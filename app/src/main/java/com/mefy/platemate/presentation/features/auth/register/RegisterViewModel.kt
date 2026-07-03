package com.mefy.platemate.presentation.features.auth.register

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.domain.usecase.auth.RegisterUseCase
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.auth.register.reducer.RegisterStateReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val registerStateReducer: RegisterStateReducer,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {
    private val _uiState = MutableStateFlow(registerStateReducer.initialState())
    val uiState: StateFlow<RegisterScreenUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<RegisterUiEffect>()
    val uiEffect: SharedFlow<RegisterUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: RegisterUiAction) {
        when (action) {
            is RegisterUiAction.UsernameChanged -> onUsernameChanged(action.value)
            is RegisterUiAction.EmailChanged -> onEmailChanged(action.value)
            is RegisterUiAction.PasswordChanged -> onPasswordChanged(action.value)
            is RegisterUiAction.PrefillIdentifierReceived -> onPrefillIdentifierReceived(action.value)
            RegisterUiAction.SubmitClicked -> onRegisterClicked()
        }
    }

    private fun onUsernameChanged(value: String) {
        _uiState.update { state -> registerStateReducer.onUsernameChanged(state, value) }
    }

    private fun onEmailChanged(value: String) {
        _uiState.update { state -> registerStateReducer.onEmailChanged(state, value) }
    }

    private fun onPasswordChanged(value: String) {
        _uiState.update { state -> registerStateReducer.onPasswordChanged(state, value) }
    }

    private fun onPrefillIdentifierReceived(value: String) {
        _uiState.update { state -> registerStateReducer.onPrefillIdentifierReceived(state, value) }
    }

    private fun onRegisterClicked() {
        if (_uiState.value.isLoading) return

        val submitCandidate = prepareSubmitAttempt()
        if (guardInvalidSubmit(submitCandidate)) return

        executeSubmit(submitCandidate)
    }

    private fun prepareSubmitAttempt(): RegisterScreenUiState {
        val preparedState = registerStateReducer.prepareSubmitAttempt(_uiState.value)
        _uiState.update { preparedState }
        return preparedState
    }

    private fun guardInvalidSubmit(state: RegisterScreenUiState): Boolean {
        if (state.isSubmitEnabled) return false

        _uiState.update(registerStateReducer::onInvalidSubmit)
        showError(UiText.Resource(R.string.auth_register_form_invalid))
        return true
    }

    private fun executeSubmit(state: RegisterScreenUiState) {
        _uiState.update(registerStateReducer::onSubmitLoading)

        launch {
            when (
                val result = registerUseCase(
                    username = state.username,
                    email = state.email,
                    password = state.password
                )
            ) {
                is AppResult.Success -> onSubmitSuccess()
                is AppResult.Error -> onSubmitError(result.error)
            }
        }
    }

    private fun onSubmitError(error: AppError) {
        handleError(error)
        val fieldErrors = (error as? AppError.Api)?.fieldErrors.orEmpty()
        _uiState.update { current ->
            registerStateReducer.onSubmitError(
                state = current,
                fieldErrors = fieldErrors
            )
        }
    }

    private fun onSubmitSuccess() {
        _uiState.update(registerStateReducer::onSubmitSuccess)
        _uiEffect.emitUiEffect(RegisterUiEffect.NavigateAfterRegister)
    }
}

