package com.mefy.platemate.data.repository

import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.RecentSearch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InMemoryRecentSearchRepositoryTest {

    @Test
    fun upsertRecent_deduplicatesAndKeepsNewestFirst() = runTest {
        val repository = InMemoryRecentSearchRepository()

        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))
        repository.upsertRecent(sampleRecent(normalized = "06XYZ987", formatted = "06 XYZ 987"))
        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))

        val recent = repository.observeRecent().first()
        assertEquals(2, recent.size)
        assertEquals("34ABC123", recent.first().normalizedPlateCode)
        assertEquals("06XYZ987", recent[1].normalizedPlateCode)
    }

    @Test
    fun upsertRecent_appliesMaxRecentLimit() = runTest {
        val repository = InMemoryRecentSearchRepository()

        repeat(12) { index ->
            val code = (index + 1).toString().padStart(2, '0')
            repository.upsertRecent(sampleRecent(normalized = "${code}ABC123", formatted = "$code ABC 123"))
        }

        val recent = repository.observeRecent().first()
        assertEquals(10, recent.size)
        assertEquals("12ABC123", recent.first().normalizedPlateCode)
        assertEquals("03ABC123", recent.last().normalizedPlateCode)
    }

    @Test
    fun clearRecent_emptiesRepositoryState() = runTest {
        val repository = InMemoryRecentSearchRepository()

        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))
        repository.clearRecent()

        assertTrue(repository.observeRecent().first().isEmpty())
    }

    private fun sampleRecent(normalized: String, formatted: String): RecentSearch = RecentSearch(
        normalizedPlateCode = normalized,
        formattedPlateCode = formatted,
        cityName = "Istanbul",
        ratingAverage = 4.2,
        commentCount = 3L,
        reportTypes = listOf(
            ReportType(
                code = "SAFE",
                label = "Safe",
                description = "Safe driving",
                iconKey = "shield",
                severity = "LOW",
                colorHex = "#00AA00",
                weight = 1,
                sortOrder = 1
            )
        )
    )
}
