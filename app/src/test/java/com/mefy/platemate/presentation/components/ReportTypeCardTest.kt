package com.mefy.platemate.presentation.components

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ReportTypeCardTest {

    @Test
    fun parseColorHexOrNull_supportsRgbAndArgbFormats() {
        assertNotNull(parseColorHexOrNull("#FF6A3D"))
        assertNotNull(parseColorHexOrNull("FF6A3D"))
        assertNotNull(parseColorHexOrNull("#80FF6A3D"))
        assertNotNull(parseColorHexOrNull("80FF6A3D"))
    }

    @Test
    fun parseColorHexOrNull_returnsNullForInvalidInput() {
        assertNull(parseColorHexOrNull("not-a-color"))
        assertNull(parseColorHexOrNull("#12"))
    }

    @Test
    fun resolveReportTagColors_fallsBackWhenHexInvalid() {
        val fallbackBackground = Color(0xFF112233)
        val fallbackContent = Color(0xFFEAF0FF)

        val (background, content) = resolveReportTagColors(
            colorHex = "invalid",
            fallbackBackground = fallbackBackground,
            fallbackContent = fallbackContent
        )

        assertEquals(fallbackBackground, background)
        assertEquals(fallbackContent, content)
    }

    @Test
    fun resolveReportTagColors_usesReadableDarkTextForBrightBackground() {
        val (background, content) = resolveReportTagColors(
            colorHex = "#FFFFFF",
            fallbackBackground = Color.Black,
            fallbackContent = Color.White
        )

        assertEquals(Color(0xFFFFFFFF), background)
        assertEquals(Color(0xFF1A1A1A), content)
    }
}

