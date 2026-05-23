package com.mefy.platemate.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mefy.platemate.data.local.LanguagePreferenceStore
import com.mefy.platemate.domain.model.language.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.languageDataStore by preferencesDataStore(name = "plate_mate_language")

@Singleton
class DataStoreLanguagePreferenceStore private constructor(
    private val dataStore: DataStore<Preferences>
) : LanguagePreferenceStore {

    @Inject
    constructor(@ApplicationContext context: Context) : this(context.languageDataStore)

    internal constructor(dataStore: DataStore<Preferences>, marker: Unit = Unit) : this(dataStore)
    private val cachedLanguage = AtomicReference<AppLanguage?>(null)
    private val cacheScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun observeLanguage(): Flow<AppLanguage?> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map { preferences ->
            AppLanguage.fromHeader(preferences[LANGUAGE])
        }

    init {
        cacheScope.launch {
            observeLanguage().collect { language ->
                cachedLanguage.set(language)
            }
        }
    }

    override suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = language.headerValue
        }
        cachedLanguage.set(language)
    }

    override suspend fun getLanguageOrNull(): AppLanguage? =
        cachedLanguage.get() ?: observeLanguage().first().also { language ->
            cachedLanguage.set(language)
        }

    override fun peekLanguageOrNull(): AppLanguage? = cachedLanguage.get()

    private companion object {
        val LANGUAGE = stringPreferencesKey("app_language")
    }
}

