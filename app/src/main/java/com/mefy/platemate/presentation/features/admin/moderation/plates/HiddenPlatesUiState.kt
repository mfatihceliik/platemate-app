package com.mefy.platemate.presentation.features.admin.moderation.plates

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class HiddenPlatesUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val errorMessage: UiText? = null,
    val items: List<HiddenPlateUiModel> = emptyList(),
    val actioningId: Long? = null
) {
    val isEmpty: Boolean get() = items.isEmpty()
}