package com.mefy.platemate.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class ReviewResponseDto(
    @SerializedName("reviewId") val reviewId: Long?,
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("rating") val rating: Int?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("username") val username: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("profilePhotoUrl") val profilePhotoUrl: String?,
    @SerializedName("reportTypeCodes") val reportTypeCodes: List<String>?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)
