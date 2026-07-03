package com.mefy.platemate.presentation.features.admin.reporttypes

sealed interface ReportTypesUiAction {
    data object BackClicked : ReportTypesUiAction
    data object RetryClicked : ReportTypesUiAction
    data object AddClicked : ReportTypesUiAction
    data class EditClicked(val id: Long) : ReportTypesUiAction
    data class ActiveToggled(val id: Long, val active: Boolean) : ReportTypesUiAction
}