package com.mefy.platemate.domain.repository

import com.mefy.platemate.domain.model.search.SavedPlate
import kotlinx.coroutines.flow.Flow

interface SavedPlateRepository {
    fun observeSavedPlates(): Flow<List<SavedPlate>>
    fun observeSavedPlateCodes(): Flow<Set<String>>
    suspend fun toggleSaved(plate: SavedPlate): Boolean

    /** Replace the local cache for the current user with the server's saved plates. */
    suspend fun replaceFromRemote(plates: List<SavedPlate>)
}
