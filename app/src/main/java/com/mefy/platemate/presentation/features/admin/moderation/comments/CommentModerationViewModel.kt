package com.mefy.platemate.presentation.features.admin.moderation.comments

import android.util.Log
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PendingComment
import com.mefy.platemate.domain.usecase.admin.ApproveCommentUseCase
import com.mefy.platemate.domain.usecase.admin.GetPendingCommentsUseCase
import com.mefy.platemate.domain.usecase.admin.RejectCommentUseCase
import com.mefy.platemate.domain.usecase.admin.RemoveCommentUseCase
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
class CommentModerationViewModel @Inject constructor(
    private val getPendingCommentsUseCase: GetPendingCommentsUseCase,
    private val approveCommentUseCase: ApproveCommentUseCase,
    private val rejectCommentUseCase: RejectCommentUseCase,
    private val removeCommentUseCase: RemoveCommentUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(CommentModerationUiState())
    val uiState: StateFlow<CommentModerationUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CommentModerationUiEffect>()
    val uiEffect: SharedFlow<CommentModerationUiEffect> = _uiEffect.asSharedFlow()

    private var currentPage = 0
    private var hasNext = true

    init {
        loadFirstPage()
    }

    // Geçici teşhis logu — 2. üst üste onaylamada Chucker'ın IOException: Canceled gösterdiği bug
    // için: VM bu iki tıklama arasında yeniden mi yaratılıyor (onCleared araya mı giriyor) kontrolü.
    // Kök neden doğrulanınca kaldırılacak.
    override fun onCleared() {
        Log.d(TAG, "onCleared instance=${System.identityHashCode(this)}")
        super.onCleared()
    }

    fun onAction(action: CommentModerationUiAction) {
        when (action) {
            CommentModerationUiAction.BackClicked -> _uiEffect.emitUiEffect(CommentModerationUiEffect.NavigateBack)
            CommentModerationUiAction.RetryClicked -> loadFirstPage()
            CommentModerationUiAction.LoadMore -> loadNextPage()
            is CommentModerationUiAction.ApproveClicked -> approve(action.commentId)
            is CommentModerationUiAction.RejectClicked -> openReasonDialog(action.commentId, ModerationAction.REJECT)
            is CommentModerationUiAction.RemoveClicked -> openReasonDialog(action.commentId, ModerationAction.REMOVE)
            is CommentModerationUiAction.ReasonChanged ->
                _uiState.update { it.copy(reasonDialog = it.reasonDialog?.copy(reason = action.value)) }
            CommentModerationUiAction.ReasonConfirmed -> confirmReason()
            CommentModerationUiAction.ReasonDismissed -> _uiState.update { it.copy(reasonDialog = null) }
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
            when (val result = getPendingCommentsUseCase(currentPage)) {
                is AppResult.Success -> {
                    hasNext = result.data.meta.hasNext
                    _uiState.update {
                        it.copy(isLoading = false, items = result.data.items.map(PendingComment::toUiModel))
                    }
                }
                is AppResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
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
            when (val result = getPendingCommentsUseCase(currentPage + 1)) {
                is AppResult.Success -> {
                    currentPage += 1
                    hasNext = result.data.meta.hasNext
                    _uiState.update {
                        it.copy(isLoadingMore = false, items = it.items + result.data.items.map(PendingComment::toUiModel))
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun approve(commentId: Long) {
        runModeration(commentId, R.string.admin_comment_approved) { approveCommentUseCase(commentId) }
    }

    private fun openReasonDialog(commentId: Long, action: ModerationAction) {
        _uiState.update { it.copy(reasonDialog = ReasonDialogState(commentId = commentId, action = action)) }
    }

    private fun confirmReason() {
        val dialog = _uiState.value.reasonDialog ?: return
        val reason = dialog.reason.trim().takeIf { it.isNotBlank() }
        _uiState.update { it.copy(reasonDialog = null) }
        when (dialog.action) {
            ModerationAction.REJECT ->
                runModeration(dialog.commentId, R.string.admin_comment_rejected) {
                    rejectCommentUseCase(dialog.commentId, reason)
                }
            ModerationAction.REMOVE ->
                runModeration(dialog.commentId, R.string.admin_comment_removed) {
                    removeCommentUseCase(dialog.commentId, reason)
                }
        }
    }

    private fun runModeration(
        commentId: Long,
        successMessageRes: Int,
        block: suspend () -> AppResult<Unit>
    ) {
        if (_uiState.value.actioningId != null) return
        _uiState.update { it.copy(actioningId = commentId) }
        Log.d(TAG, "runModeration start id=$commentId instance=${System.identityHashCode(this)}")
        launch(onError = { error -> handleError(error) }) {
            // finally: success/error/cancellation üçünde de actioningId serbest bırakılır.
            // Öncesinde sadece success/error dallarında sıfırlanıyordu — coroutine iptal
            // edilirse (ör. IOException: Canceled'e yol açan Job iptali) actioningId sonsuza
            // kadar takılı kalıp listedeki TÜM aksiyonları kilitliyordu.
            try {
                when (val result = block()) {
                    is AppResult.Success -> {
                        _uiState.update {
                            it.copy(items = it.items.filterNot { item -> item.id == commentId })
                        }
                        showSuccess(UiText.Resource(successMessageRes))
                    }
                    is AppResult.Error -> showError(result.error.toUiText())
                }
            } finally {
                _uiState.update { it.copy(actioningId = null) }
            }
        }
    }

    private companion object {
        const val TAG = "CommentModeration"
    }
}

private fun PendingComment.toUiModel(): PendingCommentUiModel = PendingCommentUiModel(
    id = id,
    plateCode = plateCode,
    username = username,
    rating = rating,
    comment = comment,
    reportCount = reportCount,
    tags = reportTags,
    date = createdAt?.substringBefore("T").orEmpty()
)
