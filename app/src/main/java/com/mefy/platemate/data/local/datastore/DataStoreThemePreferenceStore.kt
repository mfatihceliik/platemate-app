package com.mefy.platemate.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.themeDataStore by preferencesDataStore(name = "plate_mate_theme")

@Singleton
class DataStoreThemePreferenceStore private constructor(
    private val dataStore: DataStore<Preferences>
) : ThemePreferenceStore {

    @Inject
    constructor(@ApplicationContext context: Context) : this(context.themeDataStore)

    internal constructor(dataStore: DataStore<Preferences>, marker: Unit = Unit) : this(dataStore)

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

    override fun observeThemeMode(): Flow<AppThemeMode> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }
        .map { preferences ->
            AppThemeMode.fromName(preferences[THEME_MODE])
        }

    override suspend fun setThemeMode(themeMode: AppThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode.name
        }
        cachedTheme.set(themeMode)
    }

    override suspend fun getThemeMode(): AppThemeMode =
        observeThemeMode().first().also { theme ->
            cachedTheme.set(theme)
        }

    override fun peekThemeMode(): AppThemeMode = cachedTheme.get()

    override fun observeAccentColorArgb(): Flow<Long> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }
        .map { preferences ->
            preferences[ACCENT_COLOR] ?: DEFAULT_ACCENT_ARGB
        }

    override suspend fun setAccentColorArgb(argb: Long) {
        dataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = argb
        }
        cachedAccentArgb.set(argb)
    }

    override fun peekAccentColorArgb(): Long = cachedAccentArgb.get()

    private companion object {
        val THEME_MODE = stringPreferencesKey("app_theme_mode")
        val ACCENT_COLOR = longPreferencesKey("app_accent_color")
        // 0xFF06B6D4 teal — ARGB packed as signed Long
        val DEFAULT_ACCENT_ARGB: Long = android.graphics.Color.parseColor("#FF06B6D4").toLong()
    }
}
