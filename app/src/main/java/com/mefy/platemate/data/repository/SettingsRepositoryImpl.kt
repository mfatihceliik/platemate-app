package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.flatMapSuspend
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.common.result.toResultOr
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.SettingsOverviewMapper
import com.mefy.platemate.data.remote.rest.service.SettingsApiService
import com.mefy.platemate.data.remote.dto.settings.UpdateSettingsRequest
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.settings.UserSettings
import com.mefy.platemate.domain.model.settings.SettingsOverview
import com.mefy.platemate.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl @Inject constructor(
    private val api: SettingsApiService,
    private val sessionStore: SessionStore,
    private val settingsOverviewMapper: SettingsOverviewMapper,
    private val appDispatchers: AppDispatchers
) : SettingsRepository {

    override suspend fun getSettingsOverview(): AppResult<SettingsOverview> =
        withContext(appDispatchers.io) {
            currentUserIdResult().flatMapSuspend { userId ->
                safeApiCall { api.getSettingsOverview(userId) }.map(settingsOverviewMapper::map)
            }
        }

    override suspend fun updateSettings(settings: UserSettings): AppResult<Unit> =
        withContext(appDispatchers.io) {
            currentUserIdResult().flatMapSuspend { userId ->
                safeResultCall {
                    api.updateSettings(
                        userId,
                        UpdateSettingsRequest(
                            messagingEnabled = settings.messagingEnabled,
                            onlineVisibilityEnabled = settings.onlineVisibilityEnabled,
                            messageNotificationsEnabled = settings.messageNotificationsEnabled,
                            friendNotificationsEnabled = settings.friendNotificationsEnabled,
                            plateReviewNotificationsEnabled = settings.plateReviewNotificationsEnabled,
                            newFollowerNotificationsEnabled = settings.newFollowerNotificationsEnabled,
                            reviewReplyNotificationsEnabled = settings.reviewReplyNotificationsEnabled,
                            followingListVisible = settings.followingListVisible
                        )
                    )
                }
            }
        }

    private suspend fun currentUserIdResult(): AppResult<Long> =
        withContext(appDispatchers.io) {
            sessionStore.session.first()?.userId.toResultOr(AppError.SessionExpired)
        }
}




