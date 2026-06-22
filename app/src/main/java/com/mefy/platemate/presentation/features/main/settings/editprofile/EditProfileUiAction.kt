package com.mefy.platemate.presentation.features.main.settings.editprofile

sealed interface EditProfileUiAction {
    data object BackClicked : EditProfileUiAction
    data object SaveClicked : EditProfileUiAction
    data object AvatarEditClicked : EditProfileUiAction
    data object DeleteAccountClicked : EditProfileUiAction
    data object RetryClicked : EditProfileUiAction
    data class DisplayNameChanged(val value: String) : EditProfileUiAction
    data class UsernameChanged(val value: String) : EditProfileUiAction
    data class BioChanged(val value: String) : EditProfileUiAction
    data class InstagramChanged(val value: String) : EditProfileUiAction
    data class TwitterChanged(val value: String) : EditProfileUiAction
    data class LinkedInChanged(val value: String) : EditProfileUiAction
    data class WebsiteChanged(val value: String) : EditProfileUiAction
}
