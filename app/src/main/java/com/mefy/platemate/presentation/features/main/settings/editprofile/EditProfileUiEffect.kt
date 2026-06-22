package com.mefy.platemate.presentation.features.main.settings.editprofile

sealed interface EditProfileUiEffect {
    data object NavigateBack : EditProfileUiEffect
    data class ShowSnackbar(val message: String) : EditProfileUiEffect
}
