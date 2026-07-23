package com.mefy.platemate.presentation.features.main.profile.followlist

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class UserFollowListUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val selectedTab: Int = 0,
    val following: List<FollowListItemUiModel> = emptyList(),
    val followers: List<FollowListItemUiModel> = emptyList(),
    val isFollowingListHidden: Boolean = false
)
