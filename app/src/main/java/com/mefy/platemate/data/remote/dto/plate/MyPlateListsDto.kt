package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class MyPlateListsDto(
    @SerializedName("savedPlates") val savedPlates: List<PlateListItemDto>?,
    @SerializedName("alarmPlates") val alarmPlates: List<PlateListItemDto>?
)

data class PlateListItemDto(
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("cityName") val cityName: String?,
    @SerializedName("ratingAverage") val ratingAverage: Double?,
    @SerializedName("reviewCount") val reviewCount: Long?,
    @SerializedName("createdAt") val createdAt: String?
)
