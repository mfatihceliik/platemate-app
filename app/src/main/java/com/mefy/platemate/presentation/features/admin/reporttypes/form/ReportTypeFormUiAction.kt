package com.mefy.platemate.presentation.features.admin.reporttypes.form

sealed interface ReportTypeFormUiAction {
    data object BackClicked : ReportTypeFormUiAction
    data object SaveClicked : ReportTypeFormUiAction
    data class CodeChanged(val value: String) : ReportTypeFormUiAction
    data class LabelChanged(val value: String) : ReportTypeFormUiAction
    data class DescriptionChanged(val value: String) : ReportTypeFormUiAction
    data class IconKeyChanged(val value: String) : ReportTypeFormUiAction
    data class SeverityChanged(val value: String) : ReportTypeFormUiAction
    data class ColorHexChanged(val value: String) : ReportTypeFormUiAction
    data class WeightChanged(val value: String) : ReportTypeFormUiAction
    data class SortOrderChanged(val value: String) : ReportTypeFormUiAction
}