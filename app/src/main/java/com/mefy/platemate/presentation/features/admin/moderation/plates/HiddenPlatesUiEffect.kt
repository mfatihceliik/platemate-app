package com.mefy.platemate.presentation.features.admin.moderation.plates

sealed interface HiddenPlatesUiEffect {
    data object NavigateBack : HiddenPlatesUiEffect
}