package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserLocationDto(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("username") val username: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("lastUpdatedAt") val lastUpdatedAt: String
)
