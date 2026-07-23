package com.mefy.platemate.domain.model.follow

data class FollowListItem(
    val id: Long,
    val username: String,
    val displayName: String?,
    val bio: String?,
    val profilePhotoUrl: String?,
    val isFollowing: Boolean
)
