package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.flatMapSuspend
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.common.result.toResultOr
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.ThemeCatalogMapper
import com.mefy.platemate.data.remote.dto.settings.UpdateAppearanceRequest
import com.mefy.platemate.data.remote.rest.service.SettingsApiService
import com.mefy.platemate.data.remote.rest.service.ThemeApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.theme.AccentColorCatalog
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.domain.repository.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ThemeRepositoryImpl @Inject constructor(
    private val themeApi: ThemeApiService,
    private val settingsApi: SettingsApiService,
    private val sessionStore: SessionStore,
    private val themeCatalogMapper: ThemeCatalogMapper,
    private val appDispatchers: AppDispatchers
) : ThemeRepository {

    override suspend fun getCatalog(): AppResult<AccentColorCatalog> =
        withContext(appDispatchers.io) {
            safeApiCall { themeApi.getCatalog() }.map(themeCatalogMapper::map)
        }

    override suspend fun syncAppearance(themeMode: AppThemeMode, accentHex: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            currentUserIdResult().flatMapSuspend { userId ->
                safeResultCall {
                    settingsApi.updateAppearance(
                        userId,
                        UpdateAppearanceRequest(themeMode = themeMode.name, accentHex = accentHex)
                    )
                }
            }
        }

    private suspend fun currentUserIdResult(): AppResult<Long> =
        sessionStore.session.first()?.userId.toResultOr(AppError.SessionExpired)
}
