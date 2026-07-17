package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.friend.FriendshipDto
import com.mefy.platemate.data.remote.dto.friend.FriendshipStatusDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.social.Friendship
import javax.inject.Inject

class FriendshipMapper @Inject constructor() : Mapper<FriendshipDto, Friendship> {

    override fun map(input: FriendshipDto): Friendship = Friendship(
        id = input.id,
        friendUserId = input.friendUserId,
        friendUsername = input.friendUsername,
        status = FriendshipStatusDto.fromWire(input.status ?: input.statusCode).toDomain(),
        requestedAt = input.requestedAt.toAppDateTimeOrNull(),
        respondedAt = input.respondedAt.toAppDateTimeOrNull()
    )

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let { AppDateTime(it) }
}
