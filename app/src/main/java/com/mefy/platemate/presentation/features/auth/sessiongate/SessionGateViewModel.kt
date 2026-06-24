package com.mefy.platemate.presentation.features.auth.sessiongate

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.auth.RefreshSessionUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
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
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

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
                    // Oturum geçersiz -> giriş; ağ/sunucu hatası -> çevrimdışı toleransı, ana akış.
                    is AppResult.Error ->
                        if (result.error is AppError.SessionExpired) {
                            SessionGateTarget.Auth
                        } else {
                            SessionGateTarget.Main
                        }
                }
                _uiState.value = SessionGateUiState(isLoading = false, target = target)
            }
        }
    }
}
