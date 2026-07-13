package com.mefy.platemate.presentation.features.admin.commentreasons

sealed interface CommentReasonsUiEffect {
    data object NavigateBack : CommentReasonsUiEffect
    data class NavigateToForm(val reasonId: Long?) : CommentReasonsUiEffect
}
