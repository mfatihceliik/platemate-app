package com.mefy.platemate.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.local.room.PlateMateDatabase
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.model.search.SavedPlate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomSavedPlateRepositoryTest {

    private lateinit var database: PlateMateDatabase
    private lateinit var sessionStore: FakeSessionStore
    private lateinit var repository: RoomSavedPlateRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            PlateMateDatabase::class.java
        ).allowMainThreadQueries().build()
        sessionStore = FakeSessionStore()
        repository = RoomSavedPlateRepository(
            savedPlateDao = database.savedPlateDao(),
            sessionStore = sessionStore
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun observeSavedPlateCodes_returnsEmpty_whenSessionIsNull() = runBlocking {
        val codes = repository.observeSavedPlateCodes().first()
        assertTrue(codes.isEmpty())
    }

    @Test
    fun toggleSaved_savesThenUnsavesPlate() = runBlocking {
        sessionStore.setSession(sampleSession(userId = 1L))

        val saved = repository.toggleSaved(sampleSavedPlate("34ABC123", "34 ABC 123"))
        val codesAfterSave = repository.observeSavedPlateCodes().first()
        val unsaved = repository.toggleSaved(sampleSavedPlate("34ABC123", "34 ABC 123"))
        val codesAfterUnsave = repository.observeSavedPlateCodes().first()

        assertTrue(saved)
        assertTrue(codesAfterSave.contains("34ABC123"))
        assertFalse(unsaved)
        assertTrue(codesAfterUnsave.isEmpty())
    }

    @Test
    fun observeSavedPlates_switchesByActiveUser() = runBlocking {
        sessionStore.setSession(sampleSession(userId = 1L))
        repository.toggleSaved(sampleSavedPlate("34ABC123", "34 ABC 123"))

        sessionStore.setSession(sampleSession(userId = 2L))
        repository.toggleSaved(sampleSavedPlate("06XYZ987", "06 XYZ 987"))

        sessionStore.setSession(sampleSession(userId = 1L))
        val user1Items = repository.observeSavedPlates().first()
        sessionStore.setSession(sampleSession(userId = 2L))
        val user2Items = repository.observeSavedPlates().first()

        assertEquals(1, user1Items.size)
        assertEquals("34ABC123", user1Items.first().normalizedPlateCode)
        assertEquals(1, user2Items.size)
        assertEquals("06XYZ987", user2Items.first().normalizedPlateCode)
    }

    @Test
    fun clearRecent_doesNotAffectSavedPlates() = runBlocking {
        sessionStore.setSession(sampleSession(userId = 1L))
        val recentRepository = RoomRecentSearchRepository(
            recentSearchDao = database.recentSearchDao(),
            sessionStore = sessionStore
        )
        recentRepository.upsertRecent(sampleRecent("34ABC123", "34 ABC 123"))
        repository.toggleSaved(sampleSavedPlate("34ABC123", "34 ABC 123"))

        recentRepository.clearRecent()

        val savedCodes = repository.observeSavedPlateCodes().first()
        val recents = recentRepository.observeRecent().first()
        assertEquals(setOf("34ABC123"), savedCodes)
        assertTrue(recents.isEmpty())
    }

    private fun sampleSession(userId: Long): AuthSession = AuthSession(
        userId = userId,
        username = "user_$userId",
        token = "token_$userId",
        refreshToken = "refresh_$userId"
    )

    private fun sampleSavedPlate(normalized: String, formatted: String): SavedPlate = SavedPlate(
        normalizedPlateCode = normalized,
        formattedPlateCode = formatted,
        cityName = "Istanbul",
        ratingAverage = 4.2,
        commentCount = 3L,
        reportTypes = listOf(sampleReportType()),
        savedAt = 100L
    )

    private fun sampleRecent(normalized: String, formatted: String): RecentSearch = RecentSearch(
        normalizedPlateCode = normalized,
        formattedPlateCode = formatted,
        cityName = "Istanbul",
        ratingAverage = 4.2,
        commentCount = 3L,
        reportTypes = listOf(sampleReportType())
    )

    private fun sampleReportType(): ReportType = ReportType(
        code = "SAFE",
        label = "Safe",
        description = "Safe driving",
        iconKey = "shield",
        severity = "LOW",
        colorHex = "#00AA00",
        weight = 1,
        sortOrder = 1
    )

    private class FakeSessionStore : SessionStore {
        private val state = MutableStateFlow<AuthSession?>(null)

        override val session: Flow<AuthSession?> = state

        fun setSession(value: AuthSession?) {
            state.value = value
        }

        override suspend fun saveSession(session: AuthSession) {
            state.value = session
        }

        override suspend fun clearSession() {
            state.value = null
        }

        override suspend fun getToken(): String? = state.value?.token

        override fun peekToken(): String? = state.value?.token

        override suspend fun getRefreshToken(): String? = state.value?.refreshToken

        override fun peekRefreshToken(): String? = state.value?.refreshToken
    }
}
