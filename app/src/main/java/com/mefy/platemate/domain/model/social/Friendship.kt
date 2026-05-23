package com.mefy.platemate.domain.model.social

import com.mefy.platemate.domain.model.common.AppDateTime

data class Friendship(
    val id: Long,
    val friendUserId: Long,
    val friendUsername: String,
    val status: FriendshipStatus,
    val createdAt: AppDateTime?
)
