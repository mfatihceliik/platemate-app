package com.mefy.platemate.presentation.features.main.settings.themecolor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mefy.platemate.domain.usecase.theme.ObserveAccentColorUseCase
import com.mefy.platemate.domain.usecase.theme.ObserveThemeModeUseCase
import com.mefy.platemate.domain.usecase.theme.SetAccentColorUseCase
import com.mefy.platemate.domain.usecase.theme.SetThemeModeUseCase
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
class ThemeColorViewModel @Inject constructor(
    private val observeThemeModeUseCase: ObserveThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val observeAccentColorUseCase: ObserveAccentColorUseCase,
    private val setAccentColorUseCase: SetAccentColorUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(ThemeColorUiState())
    val uiState: StateFlow<ThemeColorUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ThemeColorUiEffect>()
    val uiEffect: SharedFlow<ThemeColorUiEffect> = _uiEffect.asSharedFlow()

    init {
        launch {
            observeThemeModeUseCase().collect { mode ->
                _uiState.update { it.copy(themeMode = mode) }
            }
        }
        launch {
            observeAccentColorUseCase().collect { argb ->
                _uiState.update { it.copy(selectedColor = Color(argb.toInt())) }
            }
        }
    }

    fun onAction(action: ThemeColorUiAction) {
        when (action) {
            is ThemeColorUiAction.ColorSelected -> {
                _uiState.update { it.copy(selectedColor = action.color) }
                launch(onError = ::handleError) {
                    setAccentColorUseCase(action.color.toArgb().toLong())
                }
            }

            is ThemeColorUiAction.ThemeModeSelected -> {
                launch(onError = ::handleError) {
                    setThemeModeUseCase(action.mode)
                }
            }

            ThemeColorUiAction.BackClicked ->
                _uiEffect.emitUiEffect(ThemeColorUiEffect.NavigateBack)
        }
    }
}
