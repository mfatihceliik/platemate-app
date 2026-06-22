package com.mefy.platemate.presentation.features.main.settings.language

import com.mefy.platemate.domain.usecase.language.ObserveLanguageUseCase
import com.mefy.platemate.domain.usecase.language.SetLanguageUseCase
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
class LanguageViewModel @Inject constructor(
    private val observeLanguageUseCase: ObserveLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(LanguageUiState())
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<LanguageUiEffect>()
    val uiEffect: SharedFlow<LanguageUiEffect> = _uiEffect.asSharedFlow()

    init {
        launch {
            observeLanguageUseCase().collect { language ->
                _uiState.update {
                    it.copy(
                        selectedLanguage = language ?: com.mefy.platemate.domain.model.language.AppLanguage.TR
                    )
                }
            }
        }
    }

    fun onAction(action: LanguageUiAction) {
        when (action) {
            is LanguageUiAction.SearchQueryChanged ->
                _uiState.update { it.copy(searchQuery = action.query) }

            is LanguageUiAction.LanguageSelected -> {
                launch(onError = ::handleError) {
                    setLanguageUseCase(action.language)
                }
            }

            LanguageUiAction.BackClicked ->
                _uiEffect.emitUiEffect(LanguageUiEffect.NavigateBack)
        }
    }
}
