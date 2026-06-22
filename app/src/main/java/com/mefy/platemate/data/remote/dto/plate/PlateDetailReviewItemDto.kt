package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class PlateDetailReviewItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("username") val username: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("profilePhotoUrl") val profilePhotoUrl: String?,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("reportTags") val reportTags: List<String>?,
    @SerializedName("createdAt") val createdAt: String?
)
