package com.mefy.platemate.data.remote.dto.theme

import com.google.gson.annotations.SerializedName

data class ThemeCatalogDto(
    @SerializedName("gridSize") val gridSize: Int?,
    @SerializedName("colors") val colors: List<AccentColorDto>?
)
