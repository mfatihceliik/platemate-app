package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class PlateReviewDto(
    @SerializedName("id") val id: Long,
    @SerializedName("plateCode") val plateCode: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("username") val username: String,
    @SerializedName("createdAt") val createdAt: String, // ISO 8601
    @SerializedName("updatedAt") val updatedAt: String // ISO 8601
)