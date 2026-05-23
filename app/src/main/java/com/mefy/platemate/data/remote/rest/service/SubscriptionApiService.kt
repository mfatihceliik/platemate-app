package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.user.UserDto
import com.mefy.platemate.data.remote.dto.user.UserSubscriptionDto
import com.mefy.platemate.data.remote.dto.subscription.ActivateSubscriptionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SubscriptionApiService {
    @POST("api/subscriptions/activate")
    suspend fun activateSubscription(@Body request: ActivateSubscriptionRequest): DataResultResponse<UserDto>

    @GET("api/subscriptions/me")
    suspend fun getCurrentSubscription(): DataResultResponse<UserDto>

    @GET("api/subscriptions/me/history")
    suspend fun getSubscriptionHistory(): DataResultResponse<List<UserSubscriptionDto>>
}
