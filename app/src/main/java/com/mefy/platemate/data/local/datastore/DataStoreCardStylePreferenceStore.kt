package com.mefy.platemate.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mefy.platemate.data.local.CardStylePreferenceStore
import com.mefy.platemate.domain.model.settings.PlateCardStyle
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.cardStyleDataStore by preferencesDataStore(name = "plate_mate_card_style")

@Singleton
class DataStoreCardStylePreferenceStore private constructor(
    private val dataStore: DataStore<Preferences>
) : CardStylePreferenceStore {

    @Inject
    constructor(@ApplicationContext context: Context) : this(context.cardStyleDataStore)

    internal constructor(dataStore: DataStore<Preferences>, marker: Unit = Unit) : this(dataStore)

    private val cachedStyle = AtomicReference<PlateCardStyle>(PlateCardStyle.CLASSIC)
    private val cacheScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        cacheScope.launch {
            observeCardStyle().collect { style ->
                cachedStyle.set(style)
            }
        }
    }

    override fun observeCardStyle(): Flow<PlateCardStyle> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }
        .map { preferences ->
            PlateCardStyle.fromName(preferences[CARD_STYLE])
        }

    override suspend fun setCardStyle(style: PlateCardStyle) {
        dataStore.edit { preferences ->
            preferences[CARD_STYLE] = style.name
        }
        cachedStyle.set(style)
    }

    override fun peekCardStyle(): PlateCardStyle = cachedStyle.get()

    private companion object {
        val CARD_STYLE = stringPreferencesKey("plate_card_style")
    }
}
