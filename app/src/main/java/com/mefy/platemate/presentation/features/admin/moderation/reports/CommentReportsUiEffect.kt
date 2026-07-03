package com.mefy.platemate.presentation.features.admin.moderation.reports

sealed interface CommentReportsUiEffect {
    data object NavigateBack : CommentReportsUiEffect
}