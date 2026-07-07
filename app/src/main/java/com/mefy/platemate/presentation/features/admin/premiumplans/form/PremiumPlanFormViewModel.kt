package com.mefy.platemate.presentation.features.admin.premiumplans.form

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PremiumPlanInput
import com.mefy.platemate.domain.usecase.admin.GetPremiumPlansUseCase
import com.mefy.platemate.domain.usecase.admin.SavePremiumPlanUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.AdminPremiumPlanFormDestination
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
class PremiumPlanFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPremiumPlansUseCase: GetPremiumPlansUseCase,
    private val savePremiumPlanUseCase: SavePremiumPlanUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: AdminPremiumPlanFormDestination = savedStateHandle.toRoute()
    private val planId: Long? = route.planId.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(PremiumPlanFormUiState())
    val uiState: StateFlow<PremiumPlanFormUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PremiumPlanFormUiEffect>()
    val uiEffect: SharedFlow<PremiumPlanFormUiEffect> = _uiEffect.asSharedFlow()

    init {
        if (planId != null) prefill(planId)
    }

    fun onAction(action: PremiumPlanFormUiAction) {
        when (action) {
            PremiumPlanFormUiAction.BackClicked -> _uiEffect.emitUiEffect(PremiumPlanFormUiEffect.NavigateBack)
            PremiumPlanFormUiAction.SaveClicked -> save()
            is PremiumPlanFormUiAction.TitleChanged -> _uiState.update { state -> 
                val newTitles = state.titles.toMutableMap()
                newTitles[action.locale] = action.value
                state.copy(titles = newTitles) 
            }
            is PremiumPlanFormUiAction.DescriptionChanged -> _uiState.update { state -> 
                val newDescriptions = state.descriptions.toMutableMap()
                newDescriptions[action.locale] = action.value
                state.copy(descriptions = newDescriptions) 
            }
            is PremiumPlanFormUiAction.AddLanguage -> _uiState.update { state ->
                if (state.titles.containsKey(action.locale)) return@update state
                val newTitles = state.titles.toMutableMap()
                val newDescriptions = state.descriptions.toMutableMap()
                newTitles[action.locale] = ""
                newDescriptions[action.locale] = ""
                state.copy(titles = newTitles, descriptions = newDescriptions)
            }
            is PremiumPlanFormUiAction.AmountChanged ->
                _uiState.update { it.copy(amount = action.value.filter { c -> c.isDigit() || c == '.' }.take(10)) }
            is PremiumPlanFormUiAction.CurrencyChanged ->
                _uiState.update { it.copy(currency = action.value.filter(Char::isLetter).take(3).uppercase()) }
            is PremiumPlanFormUiAction.DiscountChanged ->
                _uiState.update { it.copy(discountPercent = action.value.filter(Char::isDigit).take(3)) }
            is PremiumPlanFormUiAction.SortOrderChanged ->
                _uiState.update { it.copy(sortOrder = action.value.filter(Char::isDigit).take(4)) }
        }
    }

    private fun prefill(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false) }
            handleError(error)
        }) {
            when (val result = getPremiumPlansUseCase()) {
                is AppResult.Success -> {
                    val plan = result.data.firstOrNull { it.id == id }
                    if (plan == null) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            period = plan.period,
                            titles = plan.titles ?: mapOf("tr" to "", "en" to ""),
                            descriptions = plan.descriptions ?: mapOf("tr" to "", "en" to ""),
                            amount = formatAmount(plan.amount),
                            currency = plan.currency.ifBlank { "TRY" },
                            discountPercent = plan.discountPercent?.toString().orEmpty(),
                            sortOrder = plan.sortOrder.toString()
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
        val id = planId ?: return
        val state = _uiState.value
        if (!state.isSaveEnabled) return
        val input = PremiumPlanInput(
            titles = state.titles.mapValues { it.value.trim() },
            descriptions = state.descriptions.mapValues { it.value.trim() }.filterValues { it.isNotBlank() }.takeIf { it.isNotEmpty() },
            amount = state.amount.toDouble(),
            currency = state.currency.trim().uppercase(),
            discountPercent = state.discountPercent.toIntOrNull(),
            sortOrder = state.sortOrder.toInt()
        )
        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            when (val result = savePremiumPlanUseCase(id, input)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.admin_premium_plan_saved))
                    _uiEffect.emit(PremiumPlanFormUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun formatAmount(amount: Double): String =
        if (amount % 1.0 == 0.0) amount.toLong().toString() else amount.toString()
}
