package com.mefy.platemate.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.data.local.room.entity.SavedPlateEntity
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
class SavedPlateDaoTest {

    private lateinit var database: PlateMateDatabase
    private val dao get() = database.savedPlateDao()

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
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", savedAt = 100L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", formatted = "34-ABC-123", savedAt = 200L))

        val items = dao.observeSavedPlates(userId = 1L).first()
        assertEquals(1, items.size)
        assertEquals("34-ABC-123", items.first().formattedPlateCode)
    }

    @Test
    fun observeSavedPlates_ordersBySavedAtDesc() = runBlocking {
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", savedAt = 100L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "06XYZ987", savedAt = 300L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "16DEF456", savedAt = 200L))

        val items = dao.observeSavedPlates(userId = 1L).first()
        assertEquals(listOf("06XYZ987", "16DEF456", "34ABC123"), items.map { it.normalizedPlateCode })
    }

    @Test
    fun deleteByNormalizedPlate_removesTargetOnly() = runBlocking {
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", savedAt = 100L))
        dao.upsert(sampleEntity(userId = 1L, normalized = "06XYZ987", savedAt = 200L))

        val deleted = dao.deleteByNormalizedPlate(userId = 1L, normalizedPlateCode = "34ABC123")

        val items = dao.observeSavedPlates(userId = 1L).first()
        assertEquals(1, deleted)
        assertEquals(1, items.size)
        assertEquals("06XYZ987", items.first().normalizedPlateCode)
    }

    @Test
    fun observeSavedPlateCodes_andDelete_areUserIsolated() = runBlocking {
        dao.upsert(sampleEntity(userId = 1L, normalized = "34ABC123", savedAt = 100L))
        dao.upsert(sampleEntity(userId = 2L, normalized = "06XYZ987", savedAt = 200L))

        dao.deleteByNormalizedPlate(userId = 1L, normalizedPlateCode = "34ABC123")

        val user1Codes = dao.observeSavedPlateCodes(userId = 1L).first()
        val user2Codes = dao.observeSavedPlateCodes(userId = 2L).first()
        assertTrue(user1Codes.isEmpty())
        assertEquals(listOf("06XYZ987"), user2Codes)
    }

    private fun sampleEntity(
        userId: Long,
        normalized: String,
        formatted: String = "34 ABC 123",
        savedAt: Long
    ): SavedPlateEntity = SavedPlateEntity(
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
        savedAt = savedAt
    )
}
