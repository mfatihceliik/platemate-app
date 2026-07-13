package com.mefy.platemate.presentation.features.admin.commentreasons

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class CommentReasonsUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val items: List<CommentReasonListItem> = emptyList(),
    val togglingId: Long? = null
)
