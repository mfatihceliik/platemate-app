package com.mefy.platemate.presentation.features.admin.reporttypes

sealed interface ReportTypesUiEffect {
    data object NavigateBack : ReportTypesUiEffect
    data class NavigateToForm(val typeId: Long?) : ReportTypesUiEffect
}