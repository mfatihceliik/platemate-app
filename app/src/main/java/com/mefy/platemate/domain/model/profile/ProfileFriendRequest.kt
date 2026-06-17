package com.mefy.platemate.domain.model.profile

import com.mefy.platemate.domain.model.common.AppDateTime

data class ProfileFriendRequest(
    val id: Long,
    val requesterUserId: Long,
    val requesterUsername: String,
    val addresseeUserId: Long,
    val addresseeUsername: String,
    val statusCode: String,
    val createdAt: AppDateTime?,
    val respondedAt: AppDateTime?,
    val lastActionAt: AppDateTime?
)
