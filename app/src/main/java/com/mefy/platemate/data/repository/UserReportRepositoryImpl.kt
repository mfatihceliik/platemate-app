package com.mefy.platemate.data.repository

import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.remote.dto.user.AddUserReportRequest
import com.mefy.platemate.data.remote.rest.service.UserReportApiService
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.repository.UserReportRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class UserReportRepositoryImpl @Inject constructor(
    private val api: UserReportApiService,
    private val appDispatchers: AppDispatchers
) : UserReportRepository {

    override suspend fun reportUser(userId: Long, reason: String) =
        withContext(appDispatchers.io) {
            safeResultCall { api.reportUser(userId, AddUserReportRequest(reason)) }
        }
}
