package com.mefy.platemate.presentation.features.admin.premiumfeatures

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PremiumFeatureAdmin
import com.mefy.platemate.domain.usecase.admin.GetPremiumFeaturesUseCase
import com.mefy.platemate.domain.usecase.admin.SetPremiumFeatureActiveUseCase
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
class PremiumFeaturesViewModel @Inject constructor(
    private val getPremiumFeaturesUseCase: GetPremiumFeaturesUseCase,
    private val setPremiumFeatureActiveUseCase: SetPremiumFeatureActiveUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(PremiumFeaturesUiState())
    val uiState: StateFlow<PremiumFeaturesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PremiumFeaturesUiEffect>()
    val uiEffect: SharedFlow<PremiumFeaturesUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: PremiumFeaturesUiAction) {
        when (action) {
            PremiumFeaturesUiAction.BackClicked -> _uiEffect.emitUiEffect(PremiumFeaturesUiEffect.NavigateBack)
            PremiumFeaturesUiAction.RetryClicked -> refresh()
            PremiumFeaturesUiAction.AddClicked -> _uiEffect.emitUiEffect(PremiumFeaturesUiEffect.NavigateToForm(null))
            is PremiumFeaturesUiAction.EditClicked -> _uiEffect.emitUiEffect(PremiumFeaturesUiEffect.NavigateToForm(action.id))
            is PremiumFeaturesUiAction.ActiveToggled -> toggleActive(action.id, action.active)
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = it.items.isEmpty(), errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getPremiumFeaturesUseCase()) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, items = result.data.map(PremiumFeatureAdmin::toListItem)) }
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
            val result = setPremiumFeatureActiveUseCase(id, target)
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

private fun PremiumFeatureAdmin.toListItem(): PremiumFeatureListItem = PremiumFeatureListItem(
    id = id,
    iconKey = iconKey,
    titles = titles,
    sortOrder = sortOrder,
    active = active
)
