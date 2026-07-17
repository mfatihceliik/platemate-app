package com.mefy.platemate.presentation.features.admin.plateremovalreasons.form

sealed interface PlateRemovalReasonFormUiEffect {
    data object NavigateBack : PlateRemovalReasonFormUiEffect
}

