package com.mefy.platemate.presentation.features.admin.plateremovalreasons

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class PlateRemovalReasonsUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val items: List<PlateRemovalReasonListItem> = emptyList(),
    val togglingId: Long? = null
)

