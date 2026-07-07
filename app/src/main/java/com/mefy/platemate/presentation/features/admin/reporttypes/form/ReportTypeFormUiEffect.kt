package com.mefy.platemate.presentation.features.admin.reporttypes.form

sealed interface ReportTypeFormUiEffect {
    data object NavigateBack : ReportTypeFormUiEffect
}