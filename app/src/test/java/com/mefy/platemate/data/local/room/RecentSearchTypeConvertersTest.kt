package com.mefy.platemate.data.local.room

import com.mefy.platemate.data.local.room.model.RecentSearchReportTypeLocal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecentSearchTypeConvertersTest {

    private val converters = RecentSearchTypeConverters()

    @Test
    fun reportTypes_roundTripSerialization_preservesValues() {
        val input = listOf(
            RecentSearchReportTypeLocal(
                code = "SAFE",
                label = "Safe",
                description = "Safe driving",
                iconKey = "shield",
                severity = "LOW",
                colorHex = "#00AA00",
                weight = 1,
                sortOrder = 1
            ),
            RecentSearchReportTypeLocal(
                code = "SPEEDING",
                label = "Speeding",
                description = "Fast driving",
                iconKey = "speed",
                severity = "HIGH",
                colorHex = "#FF0000",
                weight = 8,
                sortOrder = 2
            )
        )

        val encoded = converters.fromReportTypes(input)
        val decoded = converters.toReportTypes(encoded)

        assertEquals(input, decoded)
    }

    @Test
    fun toReportTypes_returnsEmptyList_forNullOrBlankOrInvalidJson() {
        assertTrue(converters.toReportTypes(null).isEmpty())
        assertTrue(converters.toReportTypes("").isEmpty())
        assertTrue(converters.toReportTypes("invalid").isEmpty())
    }
}
