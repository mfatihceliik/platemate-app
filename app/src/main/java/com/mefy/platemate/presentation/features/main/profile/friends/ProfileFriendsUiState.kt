package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class ProfileFriendsUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val friends: List<ProfileFriendUiModel> = emptyList()
)