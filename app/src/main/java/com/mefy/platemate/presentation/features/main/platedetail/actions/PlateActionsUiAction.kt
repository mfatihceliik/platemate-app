package com.mefy.platemate.presentation.features.main.platedetail.actions

sealed interface PlateActionsUiAction {
    data object BackClicked : PlateActionsUiAction
    data object SaveToggled : PlateActionsUiAction
    data object AlarmToggled : PlateActionsUiAction
    data object ReportClicked : PlateActionsUiAction
}