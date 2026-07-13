package com.mefy.platemate.presentation.features.admin.hub

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.AdminMenuItem
import com.mefy.platemate.domain.usecase.admin.GetAdminMenuUseCase
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
class AdminHubViewModel @Inject constructor(
    private val getAdminMenuUseCase: GetAdminMenuUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(AdminHubUiState())
    val uiState: StateFlow<AdminHubUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AdminHubUiEffect>()
    val uiEffect: SharedFlow<AdminHubUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: AdminHubUiAction) {
        when (action) {
            AdminHubUiAction.BackClicked -> _uiEffect.emitUiEffect(AdminHubUiEffect.NavigateBack)
            AdminHubUiAction.RetryClicked -> refresh()
            is AdminHubUiAction.QueryChanged -> _uiState.update { it.copy(query = action.value) }
            is AdminHubUiAction.ItemClicked -> _uiEffect.emitUiEffect(AdminHubUiEffect.NavigateToItem(action.code))
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = it.items.isEmpty(), errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getAdminMenuUseCase()) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, items = result.data.map(AdminMenuItem::toUiModel)) }
                is AppResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
            }
        }
    }
}

private fun AdminMenuItem.toUiModel(): AdminMenuItemUiModel = AdminMenuItemUiModel(
    code = code,
    title = title,
    iconKey = iconKey,
    badgeCount = badgeCount
)
