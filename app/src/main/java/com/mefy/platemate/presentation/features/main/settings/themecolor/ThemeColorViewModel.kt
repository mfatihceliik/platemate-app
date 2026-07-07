package com.mefy.platemate.presentation.features.main.settings.themecolor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.theme.GetAccentCatalogUseCase
import com.mefy.platemate.domain.usecase.theme.ObserveAccentColorUseCase
import com.mefy.platemate.domain.usecase.theme.ObserveThemeModeUseCase
import com.mefy.platemate.domain.usecase.theme.SetAccentColorUseCase
import com.mefy.platemate.domain.usecase.theme.SetThemeModeUseCase
import com.mefy.platemate.domain.usecase.theme.SyncAppearanceUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.hexToColor
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.toHex
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

@HiltViewModel
class ThemeColorViewModel @Inject constructor(
    private val observeThemeModeUseCase: ObserveThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val observeAccentColorUseCase: ObserveAccentColorUseCase,
    private val setAccentColorUseCase: SetAccentColorUseCase,
    private val getAccentCatalogUseCase: GetAccentCatalogUseCase,
    private val syncAppearanceUseCase: SyncAppearanceUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(ThemeColorUiState())
    val uiState: StateFlow<ThemeColorUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ThemeColorUiEffect>()
    val uiEffect: SharedFlow<ThemeColorUiEffect> = _uiEffect.asSharedFlow()

    init {
        // Local prefs are the SAVED baseline; the draft follows the baseline only while unedited.
        launch {
            combine(
                observeThemeModeUseCase(),
                observeAccentColorUseCase()
            ) { mode, argb -> mode to argb }.collect { (mode, argb) ->
                val savedColor = Color(argb.toInt())
                _uiState.update { s ->
                    val clean = s.selectedColor == s.savedColor && s.themeMode == s.savedMode
                    s.copy(
                        savedColor = savedColor,
                        savedMode = mode,
                        selectedColor = if (clean) savedColor else s.selectedColor,
                        themeMode = if (clean) mode else s.themeMode
                    )
                }
            }
        }
        loadCatalog()
    }

    fun retry() = loadCatalog()

    private fun loadCatalog() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = { e ->
            _uiState.update { it.copy(isLoading = false) }
            handleError(e)
        }) {
            when (val result = getAccentCatalogUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        colors = result.data.colors.map(::hexToColor),
                        gridSize = result.data.gridSize
                    )
                }

                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toUiText())
                }
            }
        }
    }

    fun onAction(action: ThemeColorUiAction) {
        when (action) {
            // Selections only update the DRAFT (preview); nothing is applied/persisted until Save.
            is ThemeColorUiAction.ColorSelected ->
                _uiState.update { it.copy(selectedColor = action.color) }

            is ThemeColorUiAction.ThemeModeSelected ->
                _uiState.update { it.copy(themeMode = action.mode) }

            ThemeColorUiAction.SaveClicked -> save()

            ThemeColorUiAction.BackClicked ->
                _uiEffect.emitUiEffect(ThemeColorUiEffect.NavigateBack)
        }
    }

    private fun save() {
        val state = _uiState.value
        if (!state.isSaveEnabled) return
        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { e ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(e)
        }) {
            // Persist locally (this drives the global theme) then mirror to the backend.
            setAccentColorUseCase(state.selectedColor.toArgb().toLong())
            setThemeModeUseCase(state.themeMode)
            syncAppearanceUseCase(state.themeMode, state.selectedColor.toHex()) // non-fatal write-through
            _uiState.update { it.copy(isSaving = false) }
            showSuccess(UiText.Resource(R.string.profile_theme_saved))
        }
    }
}
