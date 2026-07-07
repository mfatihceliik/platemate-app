package com.mefy.platemate.presentation.features.admin.accentcolors.form

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.AccentColorInput
import com.mefy.platemate.domain.usecase.admin.GetAccentColorsUseCase
import com.mefy.platemate.domain.usecase.admin.SaveAccentColorUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.AdminAccentColorFormDestination
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
class AccentColorFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAccentColorsUseCase: GetAccentColorsUseCase,
    private val saveAccentColorUseCase: SaveAccentColorUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: AdminAccentColorFormDestination = savedStateHandle.toRoute()
    private val colorId: Long? = route.colorId.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(AccentColorFormUiState(isEdit = colorId != null))
    val uiState: StateFlow<AccentColorFormUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AccentColorFormUiEffect>()
    val uiEffect: SharedFlow<AccentColorFormUiEffect> = _uiEffect.asSharedFlow()

    init {
        if (colorId != null) prefill(colorId)
    }

    fun onAction(action: AccentColorFormUiAction) {
        when (action) {
            AccentColorFormUiAction.BackClicked -> _uiEffect.emitUiEffect(AccentColorFormUiEffect.NavigateBack)
            AccentColorFormUiAction.SaveClicked -> save()
            is AccentColorFormUiAction.HexChanged -> _uiState.update { it.copy(hex = sanitizeHex(action.value)) }
            is AccentColorFormUiAction.SortOrderChanged ->
                _uiState.update { it.copy(sortOrder = action.value.filter(Char::isDigit).take(4)) }
        }
    }

    private fun sanitizeHex(raw: String): String {
        val body = raw.removePrefix("#").filter { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }.take(6)
        return "#" + body.uppercase()
    }

    private fun prefill(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false) }
            handleError(error)
        }) {
            when (val result = getAccentColorsUseCase()) {
                is AppResult.Success -> {
                    val color = result.data.firstOrNull { it.id == id }
                    if (color == null) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hex = color.hex.ifBlank { "#" },
                            sortOrder = color.sortOrder.toString()
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
        val state = _uiState.value
        if (!state.isSaveEnabled) return
        val input = AccentColorInput(hex = state.hex.uppercase(), sortOrder = state.sortOrder.toInt())
        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            when (val result = saveAccentColorUseCase(colorId, input)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.admin_theme_color_saved))
                    _uiEffect.emit(AccentColorFormUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}
