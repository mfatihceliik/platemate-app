package com.mefy.platemate.presentation.features.main.profile.friends

sealed interface ProfileFriendsUiAction {
    data class SearchQueryChanged(val query: String) : ProfileFriendsUiAction
    data class FriendClicked(val friendUserId: String) : ProfileFriendsUiAction
    data class TabChanged(val tabIndex: Int) : ProfileFriendsUiAction
    data class AcceptRequestClicked(val friendshipId: Long) : ProfileFriendsUiAction
    data class RejectRequestClicked(val friendshipId: Long) : ProfileFriendsUiAction
    data class RemoveFriendClicked(val friendshipId: Long) : ProfileFriendsUiAction
    data class CancelRequestClicked(val friendshipId: Long) : ProfileFriendsUiAction
    data object ConfirmRemoveFriend : ProfileFriendsUiAction
    data object CancelRemoveFriend : ProfileFriendsUiAction
    data object BackClicked : ProfileFriendsUiAction
    data object RetryClicked : ProfileFriendsUiAction
}
