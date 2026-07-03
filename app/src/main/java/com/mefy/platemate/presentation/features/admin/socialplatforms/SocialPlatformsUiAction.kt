package com.mefy.platemate.presentation.features.admin.socialplatforms

sealed interface SocialPlatformsUiAction {
    data object BackClicked : SocialPlatformsUiAction
    data object RetryClicked : SocialPlatformsUiAction
    data object AddClicked : SocialPlatformsUiAction
    data class EditClicked(val id: Long) : SocialPlatformsUiAction
    data class ActiveToggled(val id: Long, val active: Boolean) : SocialPlatformsUiAction
}
