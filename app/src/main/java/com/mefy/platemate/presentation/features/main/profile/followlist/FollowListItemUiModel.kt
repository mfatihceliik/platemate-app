package com.mefy.platemate.presentation.features.main.profile.followlist

import androidx.compose.runtime.Immutable

@Immutable
data class FollowListItemUiModel(
    val userId: Long,
    val displayName: String,
    val username: String,
    val bio: String,
    val isFollowing: Boolean
)
