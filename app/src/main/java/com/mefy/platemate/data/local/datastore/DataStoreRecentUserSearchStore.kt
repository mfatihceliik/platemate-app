package com.mefy.platemate.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mefy.platemate.data.local.RecentUserSearchStore
import com.mefy.platemate.presentation.features.uimodel.UserSearchItemUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.recentUserSearchDataStore by preferencesDataStore(name = "recent_user_searches")

@Singleton
class DataStoreRecentUserSearchStore @Inject constructor(
    @ApplicationContext context: Context
) : RecentUserSearchStore {

    private val dataStore: DataStore<Preferences> = context.recentUserSearchDataStore

    override val recentSearches: Flow<List<UserSearchItemUiModel>> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }
        .map { preferences ->
            val jsonString = preferences[RECENT_SEARCHES_KEY] ?: "[]"
            parseJsonToList(jsonString)
        }

    override suspend fun addSearchQuery(user: UserSearchItemUiModel) {
        dataStore.edit { preferences ->
            val currentJson = preferences[RECENT_SEARCHES_KEY] ?: "[]"
            val currentList = parseJsonToList(currentJson).toMutableList()
            
            // Remove if already exists to put it at the top
            currentList.removeAll { it.id == user.id }
            
            // Add to the top
            currentList.add(0, user)
            
            // Keep only max 10
            if (currentList.size > 10) {
                currentList.removeAt(currentList.lastIndex)
            }
            
            preferences[RECENT_SEARCHES_KEY] = serializeListToJson(currentList)
        }
    }

    override suspend fun clearSearches() {
        dataStore.edit { preferences ->
            preferences.remove(RECENT_SEARCHES_KEY)
        }
    }

    private fun parseJsonToList(jsonString: String): List<UserSearchItemUiModel> {
        return try {
            val jsonArray = JSONArray(jsonString)
            List(jsonArray.length()) { i ->
                val obj = jsonArray.getJSONObject(i)
                UserSearchItemUiModel(
                    id = obj.getLong("id"),
                    username = obj.getString("username")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun serializeListToJson(list: List<UserSearchItemUiModel>): String {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("username", item.username)
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }

    private companion object {
        val RECENT_SEARCHES_KEY = stringPreferencesKey("recent_searches_list_v2")
    }
}
