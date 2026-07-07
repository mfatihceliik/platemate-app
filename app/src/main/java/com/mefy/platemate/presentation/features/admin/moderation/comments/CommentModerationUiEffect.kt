package com.mefy.platemate.presentation.features.admin.moderation.comments

sealed interface CommentModerationUiEffect {
    data object NavigateBack : CommentModerationUiEffect
}
