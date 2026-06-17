package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.friend.ProfileFriendRequestDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.profile.ProfileFriendRequest
import javax.inject.Inject

class ProfileFriendRequestMapper @Inject constructor() :
    Mapper<ProfileFriendRequestDto, ProfileFriendRequest> {

    override fun map(input: ProfileFriendRequestDto): ProfileFriendRequest = ProfileFriendRequest(
        id = input.id,
        requesterUserId = input.requesterUserId,
        requesterUsername = input.requesterUsername,
        addresseeUserId = input.addresseeUserId,
        addresseeUsername = input.addresseeUsername,
        statusCode = input.statusCode?.trim().orEmpty(),
        createdAt = input.createdAt.toAppDateTimeOrNull(),
        respondedAt = input.respondedAt.toAppDateTimeOrNull(),
        lastActionAt = input.lastActionAt.toAppDateTimeOrNull()
    )

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let(::AppDateTime)
}
