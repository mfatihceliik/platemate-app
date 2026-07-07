package com.mefy.platemate.presentation.features.admin.moderation.removal

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PlateRemovalRequest
import com.mefy.platemate.domain.usecase.admin.GetPlateRemovalRequestsUseCase
import com.mefy.platemate.domain.usecase.admin.ReviewPlateRemovalRequestUseCase
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
class PlateRemovalViewModel @Inject constructor(
    private val getPlateRemovalRequestsUseCase: GetPlateRemovalRequestsUseCase,
    private val reviewPlateRemovalRequestUseCase: ReviewPlateRemovalRequestUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(PlateRemovalUiState())
    val uiState: StateFlow<PlateRemovalUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PlateRemovalUiEffect>()
    val uiEffect: SharedFlow<PlateRemovalUiEffect> = _uiEffect.asSharedFlow()

    private var currentPage = 0
    private var hasNext = true

    init {
        loadFirstPage()
    }

    fun onAction(action: PlateRemovalUiAction) {
        when (action) {
            PlateRemovalUiAction.BackClicked -> _uiEffect.emitUiEffect(PlateRemovalUiEffect.NavigateBack)
            PlateRemovalUiAction.RetryClicked -> loadFirstPage()
            PlateRemovalUiAction.LoadMore -> loadNextPage()
            is PlateRemovalUiAction.AcceptClicked -> review(action.requestId, STATUS_ACCEPTED)
            is PlateRemovalUiAction.RejectClicked -> review(action.requestId, STATUS_REJECTED)
        }
    }

    private fun loadFirstPage() {
        currentPage = 0
        hasNext = true
        _uiState.update { it.copy(isLoading = true, errorMessage = null, items = emptyList()) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getPlateRemovalRequestsUseCase(currentPage)) {
                is AppResult.Success -> {
                    hasNext = result.data.meta.hasNext
                    _uiState.update { it.copy(isLoading = false, items = result.data.items.map(PlateRemovalRequest::toUiModel)) }
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
            }
        }
    }

    private fun loadNextPage() {
        if (_uiState.value.isLoadingMore || !hasNext) return
        _uiState.update { it.copy(isLoadingMore = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoadingMore = false) }
            handleError(error)
        }) {
            when (val result = getPlateRemovalRequestsUseCase(currentPage + 1)) {
                is AppResult.Success -> {
                    currentPage += 1
                    hasNext = result.data.meta.hasNext
                    _uiState.update { it.copy(isLoadingMore = false, items = it.items + result.data.items.map(PlateRemovalRequest::toUiModel)) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun review(requestId: Long, statusCode: String) {
        if (_uiState.value.actioningId != null) return
        _uiState.update { it.copy(actioningId = requestId) }
        launch(onError = { error ->
            _uiState.update { it.copy(actioningId = null) }
            handleError(error)
        }) {
            when (val result = reviewPlateRemovalRequestUseCase(requestId, statusCode, null)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(actioningId = null, items = it.items.filterNot { item -> item.id == requestId }) }
                    showSuccess(UiText.Resource(R.string.admin_request_reviewed))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(actioningId = null) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private companion object {
        const val STATUS_ACCEPTED = "ACCEPTED"
        const val STATUS_REJECTED = "REJECTED"
    }
}

private fun PlateRemovalRequest.toUiModel(): PlateRemovalUiModel = PlateRemovalUiModel(
    id = id,
    plateCode = plateCode,
    requesterUsername = requesterUsername,
    requesterEmail = requesterEmail,
    reasonCode = reasonCode,
    description = description,
    date = createdAt?.substringBefore("T").orEmpty()
)
