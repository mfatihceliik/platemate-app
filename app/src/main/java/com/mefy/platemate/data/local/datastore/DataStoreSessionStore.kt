package com.mefy.platemate.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.model.auth.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.sessionDataStore by preferencesDataStore(name = "plate_mate_session")

@Singleton
class DataStoreSessionStore private constructor(
    private val dataStore: DataStore<Preferences>,
    ioDispatcher: CoroutineDispatcher
) : SessionStore {

    @Inject
    constructor(
        @ApplicationContext context: Context,
        appDispatchers: AppDispatchers
    ) : this(context.sessionDataStore, appDispatchers.io)

    internal constructor(dataStore: DataStore<Preferences>, marker: Unit = Unit) :
            this(dataStore, Dispatchers.IO)

    private val cachedToken = AtomicReference<String?>(null)
    private val cachedRefreshToken = AtomicReference<String?>(null)
    private val cacheScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    override val session: Flow<AuthSession?> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map { preferences ->
            val token = preferences[TOKEN]
            val refreshToken = preferences[REFRESH_TOKEN]
            val userId = preferences[USER_ID]
            val username = preferences[USERNAME]
            if (token.isNullOrBlank() || userId == null || username.isNullOrBlank()) {
                null
            } else {
                AuthSession(
                    userId = userId,
                    username = username,
                    token = token,
                    refreshToken = refreshToken?.takeIf { it.isNotBlank() },
                    role = UserRole.fromString(preferences[ROLE])
                )
            }
        }

    init {
        cacheScope.launch {
            session.collect { currentSession ->
                cachedToken.set(currentSession?.token)
                cachedRefreshToken.set(currentSession?.refreshToken)
            }
        }
    }

    override suspend fun saveSession(session: AuthSession) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = session.userId
            preferences[USERNAME] = session.username
            preferences[TOKEN] = session.token
            preferences[ROLE] = session.role.name
            if (session.refreshToken.isNullOrBlank()) {
                preferences.remove(REFRESH_TOKEN)
            } else {
                preferences[REFRESH_TOKEN] = session.refreshToken
            }
        }
        cachedToken.set(session.token)
        cachedRefreshToken.set(session.refreshToken)
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences -> preferences.clear() }
        cachedToken.set(null)
        cachedRefreshToken.set(null)
    }

    override suspend fun getToken(): String? =
        cachedToken.get() ?: session.map { currentSession -> currentSession?.token }.first()
            .also { token -> cachedToken.set(token) }

    override fun peekToken(): String? = cachedToken.get()

    override suspend fun getRefreshToken(): String? =
        cachedRefreshToken.get() ?: session.map { currentSession -> currentSession?.refreshToken }.first()
            .also { refreshToken -> cachedRefreshToken.set(refreshToken) }

    override fun peekRefreshToken(): String? = cachedRefreshToken.get()

    private companion object {
        val USER_ID = longPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val ROLE = stringPreferencesKey("role")
    }
}

