package com.mefy.platemate.presentation.features.admin.accentcolors

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.AccentColorAdmin
import com.mefy.platemate.domain.usecase.admin.GetAccentColorsUseCase
import com.mefy.platemate.domain.usecase.admin.SaveThemeGridSizeUseCase
import com.mefy.platemate.domain.usecase.admin.SetAccentColorActiveUseCase
import com.mefy.platemate.domain.usecase.theme.GetAccentCatalogUseCase
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
class AccentColorsViewModel @Inject constructor(
    private val getAccentColorsUseCase: GetAccentColorsUseCase,
    private val setAccentColorActiveUseCase: SetAccentColorActiveUseCase,
    private val getAccentCatalogUseCase: GetAccentCatalogUseCase,
    private val saveThemeGridSizeUseCase: SaveThemeGridSizeUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(AccentColorsUiState())
    val uiState: StateFlow<AccentColorsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AccentColorsUiEffect>()
    val uiEffect: SharedFlow<AccentColorsUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: AccentColorsUiAction) {
        when (action) {
            AccentColorsUiAction.BackClicked -> _uiEffect.emitUiEffect(AccentColorsUiEffect.NavigateBack)
            AccentColorsUiAction.RetryClicked -> refresh()
            AccentColorsUiAction.AddClicked -> _uiEffect.emitUiEffect(AccentColorsUiEffect.NavigateToForm(null))
            is AccentColorsUiAction.EditClicked -> _uiEffect.emitUiEffect(AccentColorsUiEffect.NavigateToForm(action.id))
            is AccentColorsUiAction.ActiveToggled -> toggleActive(action.id, action.active)
            is AccentColorsUiAction.GridSizeChanged ->
                _uiState.update { it.copy(gridSizeInput = action.value.filter(Char::isDigit).take(1)) }
            AccentColorsUiAction.GridSizeSaveClicked -> saveGridSize()
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = it.items.isEmpty(), errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getAccentColorsUseCase()) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, items = result.data.map(AccentColorAdmin::toListItem)) }
                is AppResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
            }
            // Current grid size (from the public catalog) prefills the editor; non-fatal on error.
            when (val catalog = getAccentCatalogUseCase()) {
                is AppResult.Success -> _uiState.update { it.copy(gridSizeInput = catalog.data.gridSize.toString()) }
                is AppResult.Error -> Unit
            }
        }
    }

    private fun saveGridSize() {
        val state = _uiState.value
        if (!state.isGridSizeSaveEnabled) return
        val size = state.gridSizeInput.toInt()
        _uiState.update { it.copy(savingGridSize = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(savingGridSize = false) }
            handleError(error)
        }) {
            when (val result = saveThemeGridSizeUseCase(size)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(savingGridSize = false) }
                    showSuccess(UiText.Resource(R.string.admin_theme_grid_size_saved))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(savingGridSize = false) }
                    showError(result.error.toUiText())
                }
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
            val result = setAccentColorActiveUseCase(id, target)
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

private fun AccentColorAdmin.toListItem(): AccentColorListItem = AccentColorListItem(
    id = id,
    hex = hex,
    sortOrder = sortOrder,
    active = active
)
