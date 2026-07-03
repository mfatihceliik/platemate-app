package com.mefy.platemate.presentation.features.admin.moderation.removal


sealed interface PlateRemovalUiEffect {
    data object NavigateBack : PlateRemovalUiEffect
}