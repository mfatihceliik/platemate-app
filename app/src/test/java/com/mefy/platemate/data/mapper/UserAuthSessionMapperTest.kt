package com.mefy.platemate.data.mapper

import com.mefy.platemate.data.remote.dto.user.UserDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserAuthSessionMapperTest {

    private val mapper = UserAuthSessionMapper()

    @Test
    fun mapOrNull_returnsSession_whenAccessAndRefreshTokensExist() {
        val session = mapper.mapOrNull(
            UserDto(
                id = 7L,
                username = "fatih",
                email = "fatih@test.com",
                token = "access-token",
                refreshToken = "refresh-token",
                premiumUntil = null,
                premiumActive = false,
                roleCode = null,
                currentSubscriptionStartedAt = null,
                currentSubscriptionExpiresAt = null,
                currentSubscriptionPurchasedDays = null,
                currentSubscriptionStatus = null
            )
        )

        requireNotNull(session)
        assertEquals(7L, session.userId)
        assertEquals("fatih", session.username)
        assertEquals("access-token", session.token)
        assertEquals("refresh-token", session.refreshToken)
    }

    @Test
    fun mapOrNull_returnsNull_whenRefreshTokenMissing() {
        val session = mapper.mapOrNull(
            UserDto(
                id = 7L,
                username = "fatih",
                email = "fatih@test.com",
                token = "access-token",
                refreshToken = null,
                premiumUntil = null,
                premiumActive = false,
                roleCode = null,
                currentSubscriptionStartedAt = null,
                currentSubscriptionExpiresAt = null,
                currentSubscriptionPurchasedDays = null,
                currentSubscriptionStatus = null
            )
        )

        assertNull(session)
    }
}
