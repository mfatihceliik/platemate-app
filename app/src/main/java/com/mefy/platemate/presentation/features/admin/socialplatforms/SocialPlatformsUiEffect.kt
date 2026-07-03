package com.mefy.platemate.presentation.features.admin.socialplatforms

sealed interface SocialPlatformsUiEffect {
    data object NavigateBack : SocialPlatformsUiEffect
    data class NavigateToForm(val platformId: Long?) : SocialPlatformsUiEffect
}
