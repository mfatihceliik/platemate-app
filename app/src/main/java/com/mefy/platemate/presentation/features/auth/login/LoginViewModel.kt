package com.mefy.platemate.presentation.features.auth.login

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.domain.usecase.auth.LoginUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.auth.login.reducer.LoginStateReducer
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginStateReducer: LoginStateReducer,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState: StateFlow<LoginScreenUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<LoginUiEffect>()
    val uiEffect: SharedFlow<LoginUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: LoginUiAction) {
        when (action) {
            is LoginUiAction.EmailChanged -> onEmailChanged(action.value)
            is LoginUiAction.PasswordChanged -> onPasswordChanged(action.value)
            is LoginUiAction.PrefillEmailReceived -> onPrefillEmailReceived(action.value)
            LoginUiAction.SubmitClicked -> onLoginClicked()
        }
    }

    private fun onEmailChanged(value: String) {
        _uiState.update { state -> loginStateReducer.onEmailChanged(state, value) }
    }

    private fun onPasswordChanged(value: String) {
        _uiState.update { state -> loginStateReducer.onPasswordChanged(state, value) }
    }

    private fun onPrefillEmailReceived(value: String) {
        _uiState.update { state -> loginStateReducer.onPrefillEmailReceived(state, value) }
    }

    private fun onLoginClicked() {
        if (_uiState.value.isLoading) return

        val submitCandidate = prepareSubmitAttempt()
        if (guardInvalidSubmit(submitCandidate)) return

        executeSubmit(submitCandidate)
    }

    private fun prepareSubmitAttempt(): LoginScreenUiState {
        val preparedState = loginStateReducer.prepareSubmitAttempt(_uiState.value)
        _uiState.update { preparedState }
        return preparedState
    }

    private fun guardInvalidSubmit(state: LoginScreenUiState): Boolean {
        if (state.isSubmitEnabled) return false

        _uiState.update(loginStateReducer::onInvalidSubmit)
        showError(UiText.Resource(R.string.auth_login_form_invalid))
        return true
    }

    private fun executeSubmit(state: LoginScreenUiState) {
        _uiState.update(loginStateReducer::onSubmitLoading)

        launch {
            when (
                val result = loginUseCase(
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
            loginStateReducer.onSubmitError(
                state = current,
                fieldErrors = fieldErrors
            )
        }
    }

    private fun onSubmitSuccess() {
        _uiState.update(loginStateReducer::onSubmitSuccess)
        _uiEffect.emitUiEffect(LoginUiEffect.NavigateAfterLogin)
    }
}

