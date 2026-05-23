package com.mefy.platemate.data.remote.dto.city

import com.google.gson.annotations.SerializedName

data class CityStatsDto(
    @SerializedName("cityId") val cityId: Int,
    @SerializedName("cityName") val cityName: String?,
    @SerializedName("todayReviewCount") val todayReviewCount: Long
)