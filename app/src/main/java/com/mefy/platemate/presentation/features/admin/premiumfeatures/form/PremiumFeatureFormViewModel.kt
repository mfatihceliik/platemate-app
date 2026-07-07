package com.mefy.platemate.presentation.features.admin.premiumfeatures.form

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PremiumFeatureInput
import com.mefy.platemate.domain.usecase.admin.GetPremiumFeaturesUseCase
import com.mefy.platemate.domain.usecase.admin.SavePremiumFeatureUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.AdminPremiumFeatureFormDestination
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
class PremiumFeatureFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPremiumFeaturesUseCase: GetPremiumFeaturesUseCase,
    private val savePremiumFeatureUseCase: SavePremiumFeatureUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: AdminPremiumFeatureFormDestination = savedStateHandle.toRoute()
    private val featureId: Long? = route.featureId.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(PremiumFeatureFormUiState(isEdit = featureId != null))
    val uiState: StateFlow<PremiumFeatureFormUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PremiumFeatureFormUiEffect>()
    val uiEffect: SharedFlow<PremiumFeatureFormUiEffect> = _uiEffect.asSharedFlow()

    init {
        if (featureId != null) prefill(featureId)
    }

    fun onAction(action: PremiumFeatureFormUiAction) {
        when (action) {
            PremiumFeatureFormUiAction.BackClicked -> _uiEffect.emitUiEffect(PremiumFeatureFormUiEffect.NavigateBack)
            PremiumFeatureFormUiAction.SaveClicked -> save()
            is PremiumFeatureFormUiAction.IconKeyChanged -> _uiState.update { it.copy(iconKey = action.value) }
            is PremiumFeatureFormUiAction.TitleChanged -> _uiState.update { state -> 
                val newTitles = state.titles.toMutableMap()
                newTitles[action.locale] = action.value
                state.copy(titles = newTitles) 
            }
            is PremiumFeatureFormUiAction.SubtitleChanged -> _uiState.update { state -> 
                val newSubtitles = state.subtitles.toMutableMap()
                newSubtitles[action.locale] = action.value
                state.copy(subtitles = newSubtitles) 
            }
            is PremiumFeatureFormUiAction.AddLanguage -> _uiState.update { state ->
                if (state.titles.containsKey(action.locale)) return@update state
                val newTitles = state.titles.toMutableMap()
                val newSubtitles = state.subtitles.toMutableMap()
                newTitles[action.locale] = ""
                newSubtitles[action.locale] = ""
                state.copy(titles = newTitles, subtitles = newSubtitles)
            }
            is PremiumFeatureFormUiAction.SortOrderChanged ->
                _uiState.update { it.copy(sortOrder = action.value.filter(Char::isDigit).take(4)) }
        }
    }

    private fun prefill(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false) }
            handleError(error)
        }) {
            when (val result = getPremiumFeaturesUseCase()) {
                is AppResult.Success -> {
                    val feature = result.data.firstOrNull { it.id == id }
                    if (feature == null) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            iconKey = feature.iconKey.ifBlank { "check" },
                            titles = feature.titles,
                            subtitles = feature.subtitles ?: mapOf("tr" to "", "en" to ""),
                            sortOrder = feature.sortOrder.toString()
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
        val input = PremiumFeatureInput(
            iconKey = state.iconKey.trim().lowercase(),
            titles = state.titles.mapValues { it.value.trim() },
            subtitles = state.subtitles.mapValues { it.value.trim() }.filterValues { it.isNotBlank() }.takeIf { it.isNotEmpty() },
            sortOrder = state.sortOrder.toInt()
        )
        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            when (val result = savePremiumFeatureUseCase(featureId, input)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.admin_premium_feature_saved))
                    _uiEffect.emit(PremiumFeatureFormUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}
