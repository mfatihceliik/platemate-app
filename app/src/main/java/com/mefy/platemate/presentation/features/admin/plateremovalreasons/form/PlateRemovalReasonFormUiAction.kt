package com.mefy.platemate.presentation.features.admin.plateremovalreasons.form

sealed interface PlateRemovalReasonFormUiAction {
    data object BackClicked : PlateRemovalReasonFormUiAction
    data object SaveClicked : PlateRemovalReasonFormUiAction
    data class CodeChanged(val value: String) : PlateRemovalReasonFormUiAction
    data class LabelChanged(val value: String) : PlateRemovalReasonFormUiAction
    data class RequiresDescriptionChanged(val value: Boolean) : PlateRemovalReasonFormUiAction
    data class SortOrderChanged(val value: String) : PlateRemovalReasonFormUiAction
}

