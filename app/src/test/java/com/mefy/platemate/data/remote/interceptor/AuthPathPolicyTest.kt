package com.mefy.platemate.data.remote.interceptor

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthPathPolicyTest {

    @Test
    fun plateSearchEndpoint_isNotPublic() {
        assertFalse(AuthPathPolicy.isPublicPath("/api/plates/search"))
        assertFalse(AuthPathPolicy.isPublicPath("/api/plates/search/"))
        assertFalse(AuthPathPolicy.isPublicPath("/api/plates/search/34ABC123"))
    }

    @Test
    fun citiesEndpoints_arePublic() {
        assertTrue(AuthPathPolicy.isPublicPath("/api/cities"))
        assertTrue(AuthPathPolicy.isPublicPath("/api/cities/34"))
    }

    @Test
    fun authEndpoints_arePublic() {
        assertTrue(AuthPathPolicy.isPublicPath("/api/auth/login"))
        assertTrue(AuthPathPolicy.isPublicPath("/api/auth/register"))
        assertTrue(AuthPathPolicy.isPublicPath("/api/auth/refresh"))
        assertTrue(AuthPathPolicy.isPublicPath("/api/auth/logout"))
    }
}
