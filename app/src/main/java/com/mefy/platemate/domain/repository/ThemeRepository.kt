package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.theme.AccentColorCatalog
import com.mefy.platemate.domain.model.theme.AppThemeMode

interface ThemeRepository {
    suspend fun getCatalog(): AppResult<AccentColorCatalog>

    /** Write-through: mirror the user's local appearance choice to the backend. */
    suspend fun syncAppearance(themeMode: AppThemeMode, accentHex: String): AppResult<Unit>
}
