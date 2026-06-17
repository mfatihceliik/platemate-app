package com.mefy.platemate.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.data.local.room.entity.RecentSearchEntity
import com.mefy.platemate.data.local.room.model.RecentSearchReportTypeLocal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecentSearchDaoTest {

    private lateinit var database: PlateMateDatabase

    private val dao get() = database.recentSearchDao()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            PlateMateDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsert_deduplicatesByUserAndNormalizedPlate() = runBlocking {
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", formatted = "34 ABC 123", searchedAt = 100L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", formatted = "34-ABC-123", searchedAt = 200L))

        val items = dao.observeRecent(userId = 1L, limit = 20).first()
        assertEquals(1, items.size)
        assertEquals("34-ABC-123", items.first().formattedPlateCode)
    }

    @Test
    fun observeRecent_ordersBySearchedAtDesc() = runBlocking {
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", searchedAt = 100L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "06XYZ987", searchedAt = 300L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "16DEF456", searchedAt = 200L))

        val items = dao.observeRecent(userId = 1L, limit = 20).first()
        assertEquals(listOf("06XYZ987", "16DEF456", "34ABC123"), items.map { it.normalizedPlateCode })
    }

    @Test
    fun trimToLimit_keepsOnlyLatestTen() = runBlocking {
        repeat(12) { index ->
            val code = (index + 1).toString().padStart(2, '0')
            dao.upsert(
                sampleEntity(
                    userId = 1L,
                    normalized = "${code}ABC123",
                    formatted = "$code ABC 123",
                    searchedAt = (index + 1).toLong()
                )
            )
        }

        dao.trimToLimit(userId = 1L, limit = 10)

        val items = dao.observeRecent(userId = 1L, limit = 50).first()
        assertEquals(10, items.size)
        assertEquals("12ABC123", items.first().normalizedPlateCode)
        assertEquals("03ABC123", items.last().normalizedPlateCode)
    }

    @Test
    fun deleteRecent_removesOnlyMatchingPlateForUser() = runBlocking {
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", searchedAt = 100L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "06XYZ987", searchedAt = 200L))
        dao.upsert(sampleEntity(userId = 2L, normalized = "34ABC123", searchedAt = 300L))

        dao.deleteRecent(userId = 1L, normalizedPlateCode = "34ABC123")

        val user1Items = dao.observeRecent(userId = 1L, limit = 20).first()
        val user2Items = dao.observeRecent(userId = 2L, limit = 20).first()
        assertEquals(listOf("06XYZ987"), user1Items.map { it.normalizedPlateCode })
        assertEquals(listOf("34ABC123"), user2Items.map { it.normalizedPlateCode })
    }

    @Test
    fun clearRecent_clearsOnlyTargetUser() = runBlocking {
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", searchedAt = 100L))
        dao.upsert(sampleEntity(userId = 2L, normalized = "06XYZ987", searchedAt = 200L))

        dao.clearRecent(userId = 1L)

        val user1Items = dao.observeRecent(userId = 1L, limit = 20).first()
        val user2Items = dao.observeRecent(userId = 2L, limit = 20).first()
        assertTrue(user1Items.isEmpty())
        assertEquals(1, user2Items.size)
        assertEquals("06XYZ987", user2Items.first().normalizedPlateCode)
    }

    private fun sampleEntity(
        userId: Long,
        normalized: String,
        formatted: String = "34 ABC 123",
        searchedAt: Long
    ): RecentSearchEntity = RecentSearchEntity(
        userId = userId,
        normalizedPlateCode = normalized,
        formattedPlateCode = formatted,
        cityName = "Istanbul",
        ratingAverage = 4.2,
        commentCount = 3L,
        reportTypes = listOf(
            RecentSearchReportTypeLocal(
                code = "SAFE",
                label = "Safe",
                description = "Safe driving",
                iconKey = "shield",
                severity = "LOW",
                colorHex = "#00AA00",
                weight = 1,
                sortOrder = 1
            )
        ),
        searchedAt = searchedAt
    )
}
