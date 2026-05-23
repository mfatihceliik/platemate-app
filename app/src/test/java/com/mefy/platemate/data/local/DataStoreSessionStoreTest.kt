package com.mefy.platemate.data.local

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mefy.platemate.data.local.datastore.DataStoreSessionStore
import com.mefy.platemate.domain.model.auth.AuthSession
import java.io.File
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DataStoreSessionStoreTest {

    @Test
    fun saveSession_persistsAccessAndRefreshToken() = runBlocking {
        val store = createStore()
        val session = AuthSession(
            userId = 42L,
            username = "fatih",
            token = "access-token",
            refreshToken = "refresh-token"
        )

        store.saveSession(session)

        assertEquals("access-token", store.getToken())
        assertEquals("refresh-token", store.getRefreshToken())
        assertEquals(session, store.session.first())
    }

    @Test
    fun clearSession_clearsAccessAndRefreshToken() = runBlocking {
        val store = createStore()
        store.saveSession(
            AuthSession(
                userId = 42L,
                username = "fatih",
                token = "access-token",
                refreshToken = "refresh-token"
            )
        )

        store.clearSession()

        assertNull(store.getToken())
        assertNull(store.getRefreshToken())
        assertNull(store.session.first())
    }

    @Test
    fun saveSession_allowsLegacySessionWithoutRefreshToken() = runBlocking {
        val store = createStore()
        store.saveSession(
            AuthSession(
                userId = 42L,
                username = "fatih",
                token = "access-token",
                refreshToken = null
            )
        )

        assertEquals("access-token", store.getToken())
        assertNull(store.getRefreshToken())
        assertNull(store.session.first()?.refreshToken)
    }

    private fun createStore(): DataStoreSessionStore {
        val dir = File("C:\\tmp\\platemate-tests").apply { mkdirs() }
        val file = File(dir, "session_${UUID.randomUUID()}.preferences_pb")
        file.deleteOnExit()

        val dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            produceFile = { file }
        )

        return DataStoreSessionStore(dataStore, Unit)
    }
}
