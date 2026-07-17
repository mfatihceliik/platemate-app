package com.mefy.platemate.presentation.features.admin.plateremovalreasons

sealed interface PlateRemovalReasonsUiEffect {
    data object NavigateBack : PlateRemovalReasonsUiEffect
    data class NavigateToForm(val reasonId: Long?) : PlateRemovalReasonsUiEffect
}

