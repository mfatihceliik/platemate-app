package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.user.AddUserReportRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserReportApiService {
    @POST("api/users/{userId}/reports")
    suspend fun reportUser(
        @Path("userId") userId: Long,
        @Body request: AddUserReportRequest
    ): ResultResponse
}
