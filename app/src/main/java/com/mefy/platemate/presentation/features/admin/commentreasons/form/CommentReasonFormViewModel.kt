package com.mefy.platemate.presentation.features.admin.commentreasons.form

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.CommentReportReasonInput
import com.mefy.platemate.domain.usecase.admin.GetCommentReportReasonsAdminUseCase
import com.mefy.platemate.domain.usecase.admin.SaveCommentReportReasonUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.AdminCommentReasonFormDestination
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
class CommentReasonFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCommentReportReasonsAdminUseCase: GetCommentReportReasonsAdminUseCase,
    private val saveCommentReportReasonUseCase: SaveCommentReportReasonUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: AdminCommentReasonFormDestination = savedStateHandle.toRoute()
    private val reasonId: Long? = route.reasonId.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(CommentReasonFormUiState(isEdit = reasonId != null))
    val uiState: StateFlow<CommentReasonFormUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CommentReasonFormUiEffect>()
    val uiEffect: SharedFlow<CommentReasonFormUiEffect> = _uiEffect.asSharedFlow()

    init {
        if (reasonId != null) prefill(reasonId)
    }

    fun onAction(action: CommentReasonFormUiAction) {
        when (action) {
            CommentReasonFormUiAction.BackClicked -> _uiEffect.emitUiEffect(CommentReasonFormUiEffect.NavigateBack)
            CommentReasonFormUiAction.SaveClicked -> save()
            is CommentReasonFormUiAction.CodeChanged -> _uiState.update { it.copy(code = action.value) }
            is CommentReasonFormUiAction.LabelChanged -> _uiState.update { it.copy(label = action.value) }
            is CommentReasonFormUiAction.RequiresDescriptionChanged ->
                _uiState.update { it.copy(requiresDescription = action.value) }
            is CommentReasonFormUiAction.SortOrderChanged ->
                _uiState.update { it.copy(sortOrder = action.value.filter(Char::isDigit).take(4)) }
        }
    }

    private fun prefill(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false) }
            handleError(error)
        }) {
            when (val result = getCommentReportReasonsAdminUseCase()) {
                is AppResult.Success -> {
                    val reason = result.data.firstOrNull { it.id == id }
                    if (reason == null) {
                        _uiState.update { it.copy(isLoading = false) }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            code = reason.code,
                            label = reason.label,
                            requiresDescription = reason.requiresDescription,
                            sortOrder = reason.sortOrder.toString()
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
        val input = CommentReportReasonInput(
            code = state.code.trim(),
            label = state.label.trim(),
            requiresDescription = state.requiresDescription,
            sortOrder = state.sortOrder.toInt()
        )
        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            when (val result = saveCommentReportReasonUseCase(reasonId, input)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.admin_comment_reason_saved))
                    _uiEffect.emit(CommentReasonFormUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
}
