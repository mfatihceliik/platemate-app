package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.user.UserDto
import com.mefy.platemate.data.remote.dto.user.UserSearchResultDto
import com.mefy.platemate.data.remote.dto.user.UpdateUserRequest
import com.mefy.platemate.core.common.result.ResultResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("api/users")
    suspend fun getAllUsers(): DataResultResponse<List<UserDto>>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): DataResultResponse<UserDto>

    @GET("api/users/search")
    suspend fun searchUserByUsername(@Query("username") username: String): DataResultResponse<List<UserSearchResultDto>>

    @PUT("api/users/{userId}")
    suspend fun updateCurrentUser(
        @Path("userId") userId: Long,
        @Body request: UpdateUserRequest
    ): ResultResponse

    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): ResultResponse
}

