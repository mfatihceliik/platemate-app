package com.mefy.platemate.presentation.features.admin.moderation.comments

sealed interface CommentModerationUiAction {
    data object BackClicked : CommentModerationUiAction
    data object RetryClicked : CommentModerationUiAction
    data object LoadMore : CommentModerationUiAction
    data class ApproveClicked(val commentId: Long) : CommentModerationUiAction
    data class RejectClicked(val commentId: Long) : CommentModerationUiAction
    data class RemoveClicked(val commentId: Long) : CommentModerationUiAction
    data class ReasonChanged(val value: String) : CommentModerationUiAction
    data object ReasonConfirmed : CommentModerationUiAction
    data object ReasonDismissed : CommentModerationUiAction
}
