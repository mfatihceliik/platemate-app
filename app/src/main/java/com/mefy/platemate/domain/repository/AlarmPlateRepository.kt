package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.search.AlarmPlate
import kotlinx.coroutines.flow.Flow

interface AlarmPlateRepository {
    fun observeAlarmPlates(): Flow<List<AlarmPlate>>
    fun observeAlarmPlateCodes(): Flow<Set<String>>

    /** Toggle alarm against backend. Returns true if now alarmed, false if removed; Error on limit/network. */
    suspend fun toggleAlarm(plate: AlarmPlate): AppResult<Boolean>

    /** Replace the local cache for the current user with the server's alarm plates. */
    suspend fun replaceFromRemote(plates: List<AlarmPlate>)
}
