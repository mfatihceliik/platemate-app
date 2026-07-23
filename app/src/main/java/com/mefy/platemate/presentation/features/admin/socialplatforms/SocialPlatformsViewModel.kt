package com.mefy.platemate.presentation.features.admin.socialplatforms

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.SocialPlatformAdmin
import com.mefy.platemate.domain.usecase.admin.GetSocialPlatformsAdminUseCase
import com.mefy.platemate.domain.usecase.admin.SetSocialPlatformActiveUseCase
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
class SocialPlatformsViewModel @Inject constructor(
    private val getSocialPlatformsAdminUseCase: GetSocialPlatformsAdminUseCase,
    private val setSocialPlatformActiveUseCase: SetSocialPlatformActiveUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(SocialPlatformsUiState())
    val uiState: StateFlow<SocialPlatformsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SocialPlatformsUiEffect>()
    val uiEffect: SharedFlow<SocialPlatformsUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: SocialPlatformsUiAction) {
        when (action) {
            SocialPlatformsUiAction.BackClicked -> _uiEffect.emitUiEffect(SocialPlatformsUiEffect.NavigateBack)
            SocialPlatformsUiAction.AddClicked -> _uiEffect.emitUiEffect(SocialPlatformsUiEffect.NavigateToForm(null))
            is SocialPlatformsUiAction.EditClicked -> _uiEffect.emitUiEffect(SocialPlatformsUiEffect.NavigateToForm(action.id))
            is SocialPlatformsUiAction.ActiveToggled -> toggleActive(action.id, action.active)
        }
    }

    override fun onRetry() {
        refresh()
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = it.items.isEmpty(), errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getSocialPlatformsAdminUseCase()) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, items = result.data.map(SocialPlatformAdmin::toListItem)) }
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
            val result = setSocialPlatformActiveUseCase(id, target)
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

private fun SocialPlatformAdmin.toListItem(): SocialPlatformListItem = SocialPlatformListItem(
    id = id,
    code = code,
    labels = labels,
    sortOrder = sortOrder,
    active = active
)
