package com.mefy.platemate.presentation.features.admin.accentcolors

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class AccentColorsUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val items: List<AccentColorListItem> = emptyList(),
    val togglingId: Long? = null,
    val gridSizeInput: String = "4",
    val savingGridSize: Boolean = false
) {
    val isGridSizeValid: Boolean
        get() = gridSizeInput.toIntOrNull()?.let { it in 1..8 } == true

    val isGridSizeSaveEnabled: Boolean get() = isGridSizeValid && !savingGridSize
}
