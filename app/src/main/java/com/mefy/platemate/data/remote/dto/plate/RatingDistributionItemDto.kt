package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class RatingDistributionItemDto(
    @SerializedName("rating") val rating: Int,
    @SerializedName("count") val count: Long,
    @SerializedName("percentage") val percentage: Double
)
