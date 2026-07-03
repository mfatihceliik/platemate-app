package com.mefy.platemate.presentation.features.admin.moderation.reports

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class CommentReportsUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val errorMessage: UiText? = null,
    val items: List<CommentReportUiModel> = emptyList(),
    val actioningId: Long? = null
) {
    val isEmpty: Boolean get() = items.isEmpty()
}