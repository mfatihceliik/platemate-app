package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.fcm.RegisterFcmTokenRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmTokenApiService {
    @POST("api/fcm-tokens/register")
    suspend fun registerFcmToken(@Body request: RegisterFcmTokenRequest): ResultResponse

    @DELETE("api/fcm-tokens/unregister")
    suspend fun unregisterFcmToken(@Query("token") token: String): ResultResponse
}
