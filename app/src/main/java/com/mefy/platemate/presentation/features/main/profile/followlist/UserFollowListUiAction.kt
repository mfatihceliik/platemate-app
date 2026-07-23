package com.mefy.platemate.presentation.features.main.profile.followlist

sealed interface UserFollowListUiAction {
    data class TabChanged(val tabIndex: Int) : UserFollowListUiAction
    data class UserClicked(val userId: String) : UserFollowListUiAction
    data class FollowToggleClicked(val userId: Long, val currentlyFollowing: Boolean) : UserFollowListUiAction
    data object BackClicked : UserFollowListUiAction
}
