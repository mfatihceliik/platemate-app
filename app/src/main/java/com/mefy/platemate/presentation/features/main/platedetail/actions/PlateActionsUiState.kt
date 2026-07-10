package com.mefy.platemate.presentation.features.main.platedetail.actions

import androidx.compose.runtime.Immutable

@Immutable
data class PlateActionsUiState(
    val isLoading: Boolean = true,
    val plateCode: String = "",
    val cityCode: String = "",
    val isSaved: Boolean = false,
    val isAlarmed: Boolean = false,
    val isAlarmInProgress: Boolean = false
)