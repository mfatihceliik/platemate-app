package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class AddPlateReviewRequest(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("reportTypeCodes") val reportTypeCodes: List<String>? = null
)