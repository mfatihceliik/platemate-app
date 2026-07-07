package com.mefy.platemate.presentation.features.admin.premiumplans

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PremiumPlanAdmin
import com.mefy.platemate.domain.usecase.admin.GetPremiumPlansUseCase
import com.mefy.platemate.domain.usecase.admin.SetPremiumPlanActiveUseCase
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
class PremiumPlansViewModel @Inject constructor(
    private val getPremiumPlansUseCase: GetPremiumPlansUseCase,
    private val setPremiumPlanActiveUseCase: SetPremiumPlanActiveUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(PremiumPlansUiState())
    val uiState: StateFlow<PremiumPlansUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PremiumPlansUiEffect>()
    val uiEffect: SharedFlow<PremiumPlansUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: PremiumPlansUiAction) {
        when (action) {
            PremiumPlansUiAction.BackClicked -> _uiEffect.emitUiEffect(PremiumPlansUiEffect.NavigateBack)
            PremiumPlansUiAction.RetryClicked -> refresh()
            is PremiumPlansUiAction.EditClicked -> _uiEffect.emitUiEffect(PremiumPlansUiEffect.NavigateToForm(action.id))
            is PremiumPlansUiAction.ActiveToggled -> toggleActive(action.id, action.active)
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = it.items.isEmpty(), errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getPremiumPlansUseCase()) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, items = result.data.map(PremiumPlanAdmin::toListItem)) }
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
            val result = setPremiumPlanActiveUseCase(id, target)
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

private fun PremiumPlanAdmin.toListItem(): PremiumPlanListItem = PremiumPlanListItem(
    id = id,
    period = period,
    amount = amount,
    currency = currency,
    discountPercent = discountPercent,
    active = active
)
