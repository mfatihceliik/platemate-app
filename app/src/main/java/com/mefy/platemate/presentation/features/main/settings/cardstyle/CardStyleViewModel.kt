package com.mefy.platemate.presentation.features.main.settings.cardstyle

import com.mefy.platemate.domain.model.settings.PlateCardStyle
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.settings.ObservePlateCardStyleUseCase
import com.mefy.platemate.domain.usecase.settings.SetPlateCardStyleUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class CardStyleViewModel @Inject constructor(
    private val observePlateCardStyleUseCase: ObservePlateCardStyleUseCase,
    private val setPlateCardStyleUseCase: SetPlateCardStyleUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(CardStyleUiState())
    val uiState: StateFlow<CardStyleUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<CardStyleUiEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEffects: SharedFlow<CardStyleUiEffect> = _uiEffects.asSharedFlow()

    init {
        launch {
            observePlateCardStyleUseCase().collectLatest { style ->
                _uiState.update { it.copy(selectedStyle = style.toUiStyle()) }
            }
        }
        launch {
            observeSessionUseCase().collectLatest { session ->
                _uiState.update { it.copy(isPremium = session?.isPremium == true) }
            }
        }
    }

    fun onAction(action: CardStyleUiAction) {
        when (action) {
            is CardStyleUiAction.StyleSelected -> onStyleSelected(action.style)
            CardStyleUiAction.PremiumCtaClicked -> emitEffect(CardStyleUiEffect.NavigateToPremiumInfo)
            CardStyleUiAction.BackClicked -> emitEffect(CardStyleUiEffect.NavigateBack)
        }
    }

    private fun onStyleSelected(style: PMPlateCardStyle) {
        if (!_uiState.value.isPremium) {
            emitEffect(CardStyleUiEffect.NavigateToPremiumInfo)
            return
        }
        launch {
            setPlateCardStyleUseCase(style.toDomainStyle())
        }
    }

    private fun emitEffect(effect: CardStyleUiEffect) {
        launch { _uiEffects.emit(effect) }
    }

    private fun PlateCardStyle.toUiStyle(): PMPlateCardStyle = when (this) {
        PlateCardStyle.CLASSIC -> PMPlateCardStyle.Classic
        PlateCardStyle.SPOTLIGHT -> PMPlateCardStyle.Spotlight
        PlateCardStyle.MINIMAL -> PMPlateCardStyle.Minimal
    }

    private fun PMPlateCardStyle.toDomainStyle(): PlateCardStyle = when (this) {
        PMPlateCardStyle.Classic -> PlateCardStyle.CLASSIC
        PMPlateCardStyle.Spotlight -> PlateCardStyle.SPOTLIGHT
        PMPlateCardStyle.Minimal -> PlateCardStyle.MINIMAL
    }
}
