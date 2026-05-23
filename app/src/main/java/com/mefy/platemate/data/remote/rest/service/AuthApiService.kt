package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.user.UserDto
import com.mefy.platemate.data.remote.dto.auth.LoginRequest
import com.mefy.platemate.data.remote.dto.auth.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): DataResultResponse<UserDto>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): DataResultResponse<UserDto>
}
