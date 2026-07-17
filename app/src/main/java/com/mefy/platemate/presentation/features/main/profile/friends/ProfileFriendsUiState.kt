package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class ProfileFriendsUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val searchQuery: String = "",
    val friends: List<ProfileFriendUiModel> = emptyList(),
    val pendingRequests: List<ProfileFriendUiModel> = emptyList(),
    val sentRequests: List<ProfileFriendUiModel> = emptyList(),
    val filteredFriends: List<ProfileFriendUiModel> = emptyList(),
    val filteredPendingRequests: List<ProfileFriendUiModel> = emptyList(),
    val filteredSentRequests: List<ProfileFriendUiModel> = emptyList(),
    val selectedTab: Int = 0,
    val showRemoveFriendPopup: Boolean = false,
    val selectedFriendIdForRemoval: Long? = null,
    val isCancelingSentRequest: Boolean = false
)