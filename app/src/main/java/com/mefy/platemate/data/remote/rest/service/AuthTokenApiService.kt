package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.auth.RefreshTokenRequest
import com.mefy.platemate.data.remote.dto.user.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthTokenApiService {
    @POST("api/auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): DataResultResponse<UserDto>

    @POST("api/auth/logout")
    suspend fun logout(@Body request: RefreshTokenRequest): ResultResponse
}
