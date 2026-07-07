package com.mefy.platemate.presentation.features.admin.reporttypes

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class ReportTypesUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val items: List<ReportTypeListItem> = emptyList(),
    val togglingId: Long? = null
)
