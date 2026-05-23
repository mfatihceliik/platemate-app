package com.mefy.platemate.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class UpdatePlateReviewRequest(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String
)