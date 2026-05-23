package com.mefy.platemate.presentation.features.auth.sessiongate

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.auth.RefreshSessionUseCase
import com.mefy.platemate.presentation.common.error.ErrorContext
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import com.mefy.platemate.presentation.common.error.UiErrorUxAction
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

@HiltViewModel
class SessionGateViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val refreshSessionUseCase: RefreshSessionUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val _uiState = MutableStateFlow(SessionGateUiState())
    val uiState = _uiState.asStateFlow()

    init {
        launch {
            val session = observeSessionUseCase().first()
            if (session == null) {
                _uiState.value = SessionGateUiState(isLoading = false, target = SessionGateTarget.Auth)
            } else {
                _uiState.value = SessionGateUiState(isLoading = true, target = null)
                val target = when (val result = refreshSessionUseCase()) {
                    is AppResult.Success -> SessionGateTarget.Main
                    is AppResult.Error -> {
                        val resolvedError = handleError(
                            error = result.error,
                            context = ErrorContext.SessionGate,
                            consumeUxAction = false
                        )
                        if (resolvedError.uxAction == UiErrorUxAction.NAVIGATE_AUTH) {
                            SessionGateTarget.Auth
                        } else {
                            SessionGateTarget.Main
                        }
                    }
                }
                _uiState.value = SessionGateUiState(isLoading = false, target = target)
            }
        }
    }
}
