package com.mefy.platemate.presentation.features.main.settings.premium

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.settings.GetSettingsUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class PremiumInfoViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(PremiumInfoUiState())
    val uiState: StateFlow<PremiumInfoUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = ::handleError) {
            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            premiumActive = result.data.premiumActive,
                            premiumUntilText = result.data.premiumUntil?.take(10).orEmpty().ifBlank { "-" }
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
                }
            }
        }
    }
}
