package com.mefy.platemate.presentation.features.main.platedetail.actions

sealed interface PlateActionsUiEffect {
    data object NavigateBack : PlateActionsUiEffect
    data class NavigateToRemoval(val plateId: Long, val plateCode: String) : PlateActionsUiEffect
}