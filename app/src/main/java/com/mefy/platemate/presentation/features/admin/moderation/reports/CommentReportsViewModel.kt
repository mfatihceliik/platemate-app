package com.mefy.platemate.presentation.features.admin.moderation.reports

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.CommentReport
import com.mefy.platemate.domain.usecase.admin.GetCommentReportsUseCase
import com.mefy.platemate.domain.usecase.admin.ReviewCommentReportUseCase
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
class CommentReportsViewModel @Inject constructor(
    private val getCommentReportsUseCase: GetCommentReportsUseCase,
    private val reviewCommentReportUseCase: ReviewCommentReportUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(CommentReportsUiState())
    val uiState: StateFlow<CommentReportsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CommentReportsUiEffect>()
    val uiEffect: SharedFlow<CommentReportsUiEffect> = _uiEffect.asSharedFlow()

    private var currentPage = 0
    private var hasNext = true

    init {
        loadFirstPage()
    }

    fun onAction(action: CommentReportsUiAction) {
        when (action) {
            CommentReportsUiAction.BackClicked -> _uiEffect.emitUiEffect(CommentReportsUiEffect.NavigateBack)
            CommentReportsUiAction.RetryClicked -> loadFirstPage()
            CommentReportsUiAction.LoadMore -> loadNextPage()
            is CommentReportsUiAction.AcceptClicked -> review(action.reportId, STATUS_ACCEPTED)
            is CommentReportsUiAction.RejectClicked -> review(action.reportId, STATUS_REJECTED)
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
            when (val result = getCommentReportsUseCase(currentPage)) {
                is AppResult.Success -> {
                    hasNext = result.data.meta.hasNext
                    _uiState.update { it.copy(isLoading = false, items = result.data.items.map(CommentReport::toUiModel)) }
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
            when (val result = getCommentReportsUseCase(currentPage + 1)) {
                is AppResult.Success -> {
                    currentPage += 1
                    hasNext = result.data.meta.hasNext
                    _uiState.update { it.copy(isLoadingMore = false, items = it.items + result.data.items.map(CommentReport::toUiModel)) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun review(reportId: Long, statusCode: String) {
        if (_uiState.value.actioningId != null) return
        _uiState.update { it.copy(actioningId = reportId) }
        launch(onError = { error ->
            _uiState.update { it.copy(actioningId = null) }
            handleError(error)
        }) {
            when (val result = reviewCommentReportUseCase(reportId, statusCode, null)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(actioningId = null, items = it.items.filterNot { item -> item.id == reportId }) }
                    showSuccess(UiText.Resource(R.string.admin_report_reviewed))
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

private fun CommentReport.toUiModel(): CommentReportUiModel = CommentReportUiModel(
    id = id,
    plateCode = plateCode,
    reasonCode = reasonCode,
    description = description,
    date = createdAt?.substringBefore("T").orEmpty()
)
