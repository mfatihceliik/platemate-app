package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class PlateDto(
    @SerializedName("id") val id: Long,
    @SerializedName("plateCode") val plateCode: String,
    @SerializedName("cityName") val cityName: String?,
    @SerializedName("ratingAverage") val ratingAverage: Double,
    @SerializedName("reviewCount") val reviewCount: Int,
    @SerializedName("totalRatingSum") val totalRatingSum: Int
)
