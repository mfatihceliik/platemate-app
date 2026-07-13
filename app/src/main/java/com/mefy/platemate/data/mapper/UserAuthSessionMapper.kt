package com.mefy.platemate.data.mapper

import com.mefy.platemate.data.remote.dto.user.UserDto
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.model.auth.UserRole
import javax.inject.Inject

class UserAuthSessionMapper @Inject constructor() {
    fun mapOrNull(input: UserDto): AuthSession? {
        val accessToken = input.token?.takeIf { it.isNotBlank() } ?: return null
        val refreshToken = input.refreshToken?.takeIf { it.isNotBlank() } ?: return null
        return AuthSession(
            userId = input.id,
            username = input.username,
            token = accessToken,
            refreshToken = refreshToken,
            role = UserRole.fromString(input.roleCode?.name),
            premiumActive = input.premiumActive
        )
    }
}
