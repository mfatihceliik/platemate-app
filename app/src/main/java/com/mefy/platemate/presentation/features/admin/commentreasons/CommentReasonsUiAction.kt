package com.mefy.platemate.presentation.features.admin.commentreasons

sealed interface CommentReasonsUiAction {
    data object BackClicked : CommentReasonsUiAction
    data object RetryClicked : CommentReasonsUiAction
    data object AddClicked : CommentReasonsUiAction
    data class EditClicked(val id: Long) : CommentReasonsUiAction
    data class ActiveToggled(val id: Long, val active: Boolean) : CommentReasonsUiAction
}
