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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomRecentSearchRepositoryTest {

    private lateinit var database: PlateMateDatabase
    private lateinit var sessionStore: FakeSessionStore
    private lateinit var repository: RoomRecentSearchRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            PlateMateDatabase::class.java
        ).allowMainThreadQueries().build()
        sessionStore = FakeSessionStore()
        repository = RoomRecentSearchRepository(
            recentSearchDao = database.recentSearchDao(),
            sessionStore = sessionStore
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun observeRecent_returnsEmpty_whenSessionIsNull() = runBlocking {
        val items = repository.observeRecent().first()
        assertTrue(items.isEmpty())
    }

    @Test
    fun upsertRecent_persistsAndObserveReturnsSavedItem() = runBlocking {
        sessionStore.setSession(sampleSession(userId = 1L))
        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))

        val items = repository.observeRecent().first()
        assertEquals(1, items.size)
        assertEquals("34ABC123", items.first().normalizedPlateCode)
    }

    @Test
    fun observeRecent_switchesByActiveUser() = runBlocking {
        sessionStore.setSession(sampleSession(userId = 1L))
        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))

        sessionStore.setSession(sampleSession(userId = 2L))
        repository.upsertRecent(sampleRecent(normalized = "06XYZ987", formatted = "06 XYZ 987"))

        sessionStore.setSession(sampleSession(userId = 1L))
        val user1Items = repository.observeRecent().first()
        assertEquals(1, user1Items.size)
        assertEquals("34ABC123", user1Items.first().normalizedPlateCode)

        sessionStore.setSession(sampleSession(userId = 2L))
        val user2Items = repository.observeRecent().first()
        assertEquals(1, user2Items.size)
        assertEquals("06XYZ987", user2Items.first().normalizedPlateCode)
    }

    @Test
    fun clearRecent_clearsOnlyActiveUsersData() = runBlocking {
        sessionStore.setSession(sampleSession(userId = 1L))
        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))

        sessionStore.setSession(sampleSession(userId = 2L))
        repository.upsertRecent(sampleRecent(normalized = "06XYZ987", formatted = "06 XYZ 987"))

        sessionStore.setSession(sampleSession(userId = 1L))
        repository.clearRecent()
        assertTrue(repository.observeRecent().first().isEmpty())

        sessionStore.setSession(sampleSession(userId = 2L))
        val user2Items = repository.observeRecent().first()
        assertEquals(1, user2Items.size)
        assertEquals("06XYZ987", user2Items.first().normalizedPlateCode)
    }

    @Test
    fun deleteRecent_removesOnlyTargetPlateForActiveUser() = runBlocking {
        sessionStore.setSession(sampleSession(userId = 1L))
        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))
        repository.upsertRecent(sampleRecent(normalized = "06XYZ987", formatted = "06 XYZ 987"))

        sessionStore.setSession(sampleSession(userId = 2L))
        repository.upsertRecent(sampleRecent(normalized = "34ABC123", formatted = "34 ABC 123"))

        sessionStore.setSession(sampleSession(userId = 1L))
        repository.deleteRecent(normalizedPlateCode = "34ABC123")
        val user1Items = repository.observeRecent().first()
        assertEquals(1, user1Items.size)
        assertEquals("06XYZ987", user1Items.first().normalizedPlateCode)

        sessionStore.setSession(sampleSession(userId = 2L))
        val user2Items = repository.observeRecent().first()
        assertEquals(1, user2Items.size)
        assertEquals("34ABC123", user2Items.first().normalizedPlateCode)
    }

    private fun sampleSession(userId: Long): AuthSession = AuthSession(
        userId = userId,
        username = "user_$userId",
        token = "token_$userId",
        refreshToken = "refresh_$userId"
    )

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
