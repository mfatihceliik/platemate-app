package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.user.UserDto
import com.mefy.platemate.domain.model.user.User
import javax.inject.Inject

class UserMapper @Inject constructor() : Mapper<UserDto, User> {
    override fun map(input: UserDto): User = User(
        id = input.id,
        username = input.username,
        email = input.email,
        token = input.token,
        premiumUntil = input.premiumUntil,
        premiumActive = input.premiumActive,
        roleCode = input.roleCode?.name,
        currentSubscriptionStartedAt = input.currentSubscriptionStartedAt,
        currentSubscriptionExpiresAt = input.currentSubscriptionExpiresAt,
        currentSubscriptionPurchasedDays = input.currentSubscriptionPurchasedDays,
        currentSubscriptionStatus = input.currentSubscriptionStatus?.name
    )
}
