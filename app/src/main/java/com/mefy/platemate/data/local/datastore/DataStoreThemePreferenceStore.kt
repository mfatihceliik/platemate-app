package com.mefy.platemate.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.local.ThemePreferenceStore
import com.mefy.platemate.domain.model.theme.AppThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.themeDataStore by preferencesDataStore(name = "plate_mate_theme")

@Singleton
class DataStoreThemePreferenceStore private constructor(
    private val dataStore: DataStore<Preferences>,
    private val sessionStore: SessionStore
) : ThemePreferenceStore {

    @Inject
    constructor(
        @ApplicationContext context: Context,
        sessionStore: SessionStore
    ) : this(context.themeDataStore, sessionStore)

    internal constructor(
        dataStore: DataStore<Preferences>,
        sessionStore: SessionStore,
        marker: Unit = Unit
    ) : this(dataStore, sessionStore)

    private val cachedTheme = AtomicReference<AppThemeMode>(AppThemeMode.SYSTEM)
    private val cachedAccentArgb = AtomicLong(DEFAULT_ACCENT_ARGB)
    private val cacheScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        cacheScope.launch {
            observeThemeMode().collect { theme ->
                cachedTheme.set(theme)
            }
        }
        cacheScope.launch {
            observeAccentColorArgb().collect { argb ->
                cachedAccentArgb.set(argb)
            }
        }
    }

    override fun observeThemeMode(): Flow<AppThemeMode> = combine(
        dataStore.data.catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        },
        sessionStore.session
    ) { preferences, session ->
        val userId = session?.userId
        if (userId == null) {
            AppThemeMode.SYSTEM
        } else {
            val key = stringPreferencesKey("app_theme_mode_$userId")
            AppThemeMode.fromName(preferences[key])
        }
    }

    override suspend fun setThemeMode(themeMode: AppThemeMode) {
        val session = sessionStore.session.first() ?: return
        val key = stringPreferencesKey("app_theme_mode_${session.userId}")
        dataStore.edit { preferences ->
            preferences[key] = themeMode.name
        }
        cachedTheme.set(themeMode)
    }

    override suspend fun getThemeMode(): AppThemeMode =
        observeThemeMode().first().also { theme ->
            cachedTheme.set(theme)
        }

    override fun peekThemeMode(): AppThemeMode = cachedTheme.get()

    override fun observeAccentColorArgb(): Flow<Long> = combine(
        dataStore.data.catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        },
        sessionStore.session
    ) { preferences, session ->
        val userId = session?.userId
        if (userId == null) {
            DEFAULT_ACCENT_ARGB
        } else {
            val key = longPreferencesKey("app_accent_color_$userId")
            preferences[key] ?: DEFAULT_ACCENT_ARGB
        }
    }

    override suspend fun setAccentColorArgb(argb: Long) {
        val session = sessionStore.session.first() ?: return
        val key = longPreferencesKey("app_accent_color_${session.userId}")
        dataStore.edit { preferences ->
            preferences[key] = argb
        }
        cachedAccentArgb.set(argb)
    }

    override fun peekAccentColorArgb(): Long = cachedAccentArgb.get()

    private companion object {
        // 0xFF06B6D4 teal — ARGB packed as signed Long
        val DEFAULT_ACCENT_ARGB: Long = android.graphics.Color.parseColor("#FF06B6D4").toLong()
    }
}
