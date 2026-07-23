package com.mefy.platemate.presentation.features.main.settings.editprofile

sealed interface EditProfileUiAction {
    data object BackClicked : EditProfileUiAction
    data object SaveClicked : EditProfileUiAction
    data object AvatarEditClicked : EditProfileUiAction
    data class AvatarUrlChanged(val value: String) : EditProfileUiAction
    data object AvatarUrlConfirmed : EditProfileUiAction
    data object AvatarDialogDismissed : EditProfileUiAction
    data object DeleteAccountClicked : EditProfileUiAction
    data class DisplayNameChanged(val value: String) : EditProfileUiAction
    data class UsernameChanged(val value: String) : EditProfileUiAction
    data class BioChanged(val value: String) : EditProfileUiAction
    data class SocialUrlInputChanged(val value: String) : EditProfileUiAction
    data object AddSocialLinkClicked : EditProfileUiAction
    data class RemoveSocialLinkClicked(val platformCode: String) : EditProfileUiAction
}
