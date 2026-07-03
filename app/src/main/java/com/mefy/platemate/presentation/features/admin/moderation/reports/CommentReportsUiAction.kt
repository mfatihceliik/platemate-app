package com.mefy.platemate.presentation.features.admin.moderation.reports

sealed interface CommentReportsUiAction {
    data object BackClicked : CommentReportsUiAction
    data object RetryClicked : CommentReportsUiAction
    data object LoadMore : CommentReportsUiAction
    data class AcceptClicked(val reportId: Long) : CommentReportsUiAction
    data class RejectClicked(val reportId: Long) : CommentReportsUiAction
}