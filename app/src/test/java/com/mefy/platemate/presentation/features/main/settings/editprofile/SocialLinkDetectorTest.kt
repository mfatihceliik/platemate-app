package com.mefy.platemate.presentation.features.main.settings.editprofile

import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.features.uimodel.SocialPlatform
import com.mefy.platemate.presentation.features.uimodel.detectPlatform
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SocialLinkDetectorTest {

    private fun platform(code: String, baseUrl: String?) = SocialPlatform(
        id = 0,
        code = code,
        displayName = code,
        iconUrl = null,
        baseUrl = baseUrl,
        backgroundColor = Color(0xFF000000),
        iconTint = Color(0xFF000000)
    )

    private val platforms = listOf(
        platform("INSTAGRAM", "https://www.instagram.com/"),
        platform("X", "https://x.com/"),
        platform("LINKEDIN", "https://www.linkedin.com/in/"),
        platform("GITHUB", "https://github.com/")
    )

    @Test
    fun `full instagram profile url resolves to INSTAGRAM`() {
        val result = detectPlatform("https://www.instagram.com/mfatihceliik/", platforms)
        assertEquals("INSTAGRAM", result?.code)
    }

    @Test
    fun `bare host without scheme still resolves`() {
        val result = detectPlatform("x.com/foo", platforms)
        assertEquals("X", result?.code)
    }

    @Test
    fun `linkedin deep path resolves via host`() {
        val result = detectPlatform("https://www.linkedin.com/in/someone", platforms)
        assertEquals("LINKEDIN", result?.code)
    }

    @Test
    fun `unknown host returns null`() {
        assertNull(detectPlatform("https://random-site.com/x", platforms))
    }

    @Test
    fun `blank input returns null`() {
        assertNull(detectPlatform("   ", platforms))
    }

    @Test
    fun `platform without baseUrl and unrelated host returns null`() {
        val noBase = listOf(platform("FACEBOOK", null))
        assertNull(detectPlatform("https://example.com/foo", noBase))
    }

    @Test
    fun `platform without baseUrl still resolves when code matches a host label`() {
        val noBase = listOf(platform("FACEBOOK", null))
        assertEquals("FACEBOOK", detectPlatform("https://www.facebook.com/foo", noBase)?.code)
    }
}
