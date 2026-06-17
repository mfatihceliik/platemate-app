package com.mefy.platemate.data.local

import com.mefy.platemate.domain.model.theme.AppThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemePreferenceStore {
    fun observeThemeMode(): Flow<AppThemeMode>
    suspend fun setThemeMode(themeMode: AppThemeMode)
    suspend fun getThemeMode(): AppThemeMode
    fun peekThemeMode(): AppThemeMode

    fun observeAccentColorArgb(): Flow<Long>
    suspend fun setAccentColorArgb(argb: Long)
    fun peekAccentColorArgb(): Long
}
