package com.mefy.platemate.presentation.features.admin.commentreasons

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.CommentReportReasonAdmin
import com.mefy.platemate.domain.usecase.admin.GetCommentReportReasonsAdminUseCase
import com.mefy.platemate.domain.usecase.admin.SetCommentReportReasonActiveUseCase
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
class CommentReasonsViewModel @Inject constructor(
    private val getCommentReportReasonsAdminUseCase: GetCommentReportReasonsAdminUseCase,
    private val setCommentReportReasonActiveUseCase: SetCommentReportReasonActiveUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(CommentReasonsUiState())
    val uiState: StateFlow<CommentReasonsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CommentReasonsUiEffect>()
    val uiEffect: SharedFlow<CommentReasonsUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: CommentReasonsUiAction) {
        when (action) {
            CommentReasonsUiAction.BackClicked -> _uiEffect.emitUiEffect(CommentReasonsUiEffect.NavigateBack)
            CommentReasonsUiAction.RetryClicked -> refresh()
            CommentReasonsUiAction.AddClicked -> _uiEffect.emitUiEffect(CommentReasonsUiEffect.NavigateToForm(null))
            is CommentReasonsUiAction.EditClicked -> _uiEffect.emitUiEffect(CommentReasonsUiEffect.NavigateToForm(action.id))
            is CommentReasonsUiAction.ActiveToggled -> toggleActive(action.id, action.active)
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = it.items.isEmpty(), errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getCommentReportReasonsAdminUseCase()) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, items = result.data.map(CommentReportReasonAdmin::toListItem)) }
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
            val result = setCommentReportReasonActiveUseCase(id, target)
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

private fun CommentReportReasonAdmin.toListItem(): CommentReasonListItem = CommentReasonListItem(
    id = id,
    code = code,
    label = label,
    requiresDescription = requiresDescription,
    active = active
)
