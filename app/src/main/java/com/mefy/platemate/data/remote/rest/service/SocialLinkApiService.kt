package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.social.AddSocialLinkRequest
import com.mefy.platemate.data.remote.dto.social.UpdateSocialLinkRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SocialLinkApiService {
    @POST("api/social-links")
    suspend fun addSocialLink(@Body request: AddSocialLinkRequest): ResultResponse

    @PUT("api/social-links")
    suspend fun updateSocialLink(@Body request: UpdateSocialLinkRequest): ResultResponse

    @DELETE("api/social-links/{id}")
    suspend fun deleteSocialLink(@Path("id") id: Long): ResultResponse
}

