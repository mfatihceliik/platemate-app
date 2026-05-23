package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.user.UserLocationDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.location.UserLocation
import javax.inject.Inject

class UserLocationMapper @Inject constructor() : Mapper<UserLocationDto, UserLocation> {
    override fun map(input: UserLocationDto): UserLocation = UserLocation(
        id = input.id,
        userId = input.userId,
        username = input.username,
        latitude = input.latitude,
        longitude = input.longitude,
        lastUpdatedAt = input.lastUpdatedAt.toAppDateTimeOrNull()
    )

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let { AppDateTime(it) }
}


