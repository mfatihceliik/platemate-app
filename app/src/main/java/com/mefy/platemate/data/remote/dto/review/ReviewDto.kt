package com.mefy.platemate.data.remote.dto.review

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.domain.model.common.AppDateTime

data class ReviewDto(
    @SerializedName("id") val id: Long,
    @SerializedName("plateCode") val plateCode: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("username") val reviewerUsername: String,
    @SerializedName("createdAt") val createdAt: AppDateTime?,
    @SerializedName("updatedAt") val updatedAt: AppDateTime?
)