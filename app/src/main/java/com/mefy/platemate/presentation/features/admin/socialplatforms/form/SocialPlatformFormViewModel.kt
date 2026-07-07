package com.mefy.platemate.presentation.features.admin.socialplatforms.form

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.SocialPlatformInput
import com.mefy.platemate.domain.usecase.admin.GetSocialPlatformsAdminUseCase
import com.mefy.platemate.domain.usecase.admin.SaveSocialPlatformUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.AdminSocialPlatformFormDestination
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
class SocialPlatformFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSocialPlatformsAdminUseCase: GetSocialPlatformsAdminUseCase,
    private val saveSocialPlatformUseCase: SaveSocialPlatformUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: AdminSocialPlatformFormDestination = savedStateHandle.toRoute()
    private val platformId: Long? = route.platformId.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(SocialPlatformFormUiState(isEdit = platformId != null))
    val uiState: StateFlow<SocialPlatformFormUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SocialPlatformFormUiEffect>()
    val uiEffect: SharedFlow<SocialPlatformFormUiEffect> = _uiEffect.asSharedFlow()

    init {
        if (platformId != null) prefill(platformId)
    }

    fun onAction(action: SocialPlatformFormUiAction) {
        when (action) {
            SocialPlatformFormUiAction.BackClicked -> _uiEffect.emitUiEffect(SocialPlatformFormUiEffect.NavigateBack)
            SocialPlatformFormUiAction.SaveClicked -> save()
            is SocialPlatformFormUiAction.CodeChanged -> _uiState.update { it.copy(code = action.value) }
            is SocialPlatformFormUiAction.LabelChanged -> _uiState.update { state -> 
                val newLabels = state.labels.toMutableMap()
                newLabels[action.locale] = action.value
                state.copy(labels = newLabels) 
            }
            is SocialPlatformFormUiAction.AddLabelLanguage -> _uiState.update { state ->
                if (state.labels.containsKey(action.locale)) return@update state
                val newLabels = state.labels.toMutableMap()
                newLabels[action.locale] = ""
                state.copy(labels = newLabels)
            }
            is SocialPlatformFormUiAction.IconUrlChanged -> _uiState.update { it.copy(iconUrl = action.value) }
            is SocialPlatformFormUiAction.BackgroundColorHexChanged -> _uiState.update { it.copy(backgroundColorHex = action.value) }
            is SocialPlatformFormUiAction.IconTintColorHexChanged -> _uiState.update { it.copy(iconTintColorHex = action.value) }
            is SocialPlatformFormUiAction.SortOrderChanged -> _uiState.update { it.copy(sortOrder = action.value.filter(Char::isDigit).take(4)) }
        }
    }

    private fun prefill(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false) }
            handleError(error)
        }) {
            when (val result = getSocialPlatformsAdminUseCase()) {
                is AppResult.Success -> {
                    val platform = result.data.firstOrNull { it.id == id }
                    if (platform == null) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            code = platform.code,
                            labels = platform.labels,
                            iconUrl = platform.iconUrl,
                            backgroundColorHex = platform.backgroundColorHex,
                            iconTintColorHex = platform.iconTintColorHex,
                            sortOrder = platform.sortOrder.toString()
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
        val input = SocialPlatformInput(
            code = state.code.trim(),
            labels = state.labels.mapValues { it.value.trim() },
            iconUrl = state.iconUrl.trim(),
            backgroundColorHex = state.backgroundColorHex.trim(),
            iconTintColorHex = state.iconTintColorHex.trim(),
            sortOrder = state.sortOrder.toInt()
        )
        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            when (val result = saveSocialPlatformUseCase(platformId, input)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.admin_social_platform_saved))
                    _uiEffect.emit(SocialPlatformFormUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}
