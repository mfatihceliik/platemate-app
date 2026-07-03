package com.mefy.platemate.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.mefy.platemate.data.local.NotificationPermissionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.notificationPermissionDataStore by preferencesDataStore(
    name = "plate_mate_notification_permission"
)

@Singleton
class DataStoreNotificationPermissionStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) : NotificationPermissionStore {

    private val dataStore get() = context.notificationPermissionDataStore

    override suspend fun hasRequestedOnMessages(): Boolean = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }
        .map { preferences -> preferences[REQUESTED_ON_MESSAGES] ?: false }
        .first()

    override suspend fun setRequestedOnMessages() {
        dataStore.edit { preferences -> preferences[REQUESTED_ON_MESSAGES] = true }
    }

    private companion object {
        val REQUESTED_ON_MESSAGES = booleanPreferencesKey("requested_on_messages")
    }
}
