package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileFriendsUiState(
    val isLoading: Boolean = true,
    val friends: List<ProfileFriendUiModel> = emptyList()
)

@Immutable
data class ProfileFriendUiModel(
    val id: Long,
    val username: String,
    val status: String,
    val createdAtText: String
)
