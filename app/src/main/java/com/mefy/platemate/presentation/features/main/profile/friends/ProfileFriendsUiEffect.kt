package com.mefy.platemate.presentation.features.main.profile.friends

sealed interface ProfileFriendsUiEffect {
    data object NavigateBack : ProfileFriendsUiEffect
    data class NavigateToUserProfile(val userId: String) : ProfileFriendsUiEffect
}
