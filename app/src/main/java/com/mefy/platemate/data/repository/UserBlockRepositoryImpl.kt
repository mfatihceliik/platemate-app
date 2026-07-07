package com.mefy.platemate.data.repository

import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.remote.rest.service.UserBlockApiService
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.repository.UserBlockRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class UserBlockRepositoryImpl @Inject constructor(
    private val api: UserBlockApiService,
    private val appDispatchers: AppDispatchers
) : UserBlockRepository {

    override suspend fun blockUser(userId: Long) =
        withContext(appDispatchers.io) {
            safeResultCall { api.blockUser(userId) }
        }

    override suspend fun unblockUser(userId: Long) =
        withContext(appDispatchers.io) {
            safeResultCall { api.unblockUser(userId) }
        }
}
