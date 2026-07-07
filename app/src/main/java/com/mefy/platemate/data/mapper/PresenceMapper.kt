package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.chat.PresenceDto
import com.mefy.platemate.domain.model.chat.UserPresence
import javax.inject.Inject

class PresenceMapper @Inject constructor() : Mapper<PresenceDto, UserPresence> {
    override fun map(input: PresenceDto): UserPresence = UserPresence(
        userId = input.userId,
        online = input.online
    )
}
