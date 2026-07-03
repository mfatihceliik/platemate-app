package com.mefy.platemate.presentation.features.admin.moderation.comments

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class CommentModerationUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val errorMessage: UiText? = null,
    val items: List<PendingCommentUiModel> = emptyList(),
    val actioningId: Long? = null,
    val reasonDialog: ReasonDialogState? = null
) {
    val isEmpty: Boolean get() = items.isEmpty()
}
