package com.mefy.platemate.presentation.features.main.platedetail.removal

sealed interface PlateRemovalRequestUiEffect {
    data object NavigateBack : PlateRemovalRequestUiEffect
}