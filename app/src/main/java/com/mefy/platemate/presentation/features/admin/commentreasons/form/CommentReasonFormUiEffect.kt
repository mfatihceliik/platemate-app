package com.mefy.platemate.presentation.features.admin.commentreasons.form

sealed interface CommentReasonFormUiEffect {
    data object NavigateBack : CommentReasonFormUiEffect
}
