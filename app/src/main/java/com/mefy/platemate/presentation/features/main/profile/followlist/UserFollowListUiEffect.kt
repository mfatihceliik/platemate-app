package com.mefy.platemate.presentation.features.main.profile.followlist

sealed interface UserFollowListUiEffect {
    data object NavigateBack : UserFollowListUiEffect
    data class NavigateToUserProfile(val userId: String) : UserFollowListUiEffect
}
