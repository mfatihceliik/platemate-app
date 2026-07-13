package com.mefy.platemate.presentation.features.admin.commentreasons.form

sealed interface CommentReasonFormUiAction {
    data object BackClicked : CommentReasonFormUiAction
    data object SaveClicked : CommentReasonFormUiAction
    data class CodeChanged(val value: String) : CommentReasonFormUiAction
    data class LabelChanged(val value: String) : CommentReasonFormUiAction
    data class RequiresDescriptionChanged(val value: Boolean) : CommentReasonFormUiAction
    data class SortOrderChanged(val value: String) : CommentReasonFormUiAction
}
