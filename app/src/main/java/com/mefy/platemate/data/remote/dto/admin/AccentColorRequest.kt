package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

/** Shared body for adding and updating an accent color. */
data class AccentColorRequest(
    @SerializedName("hex") val hex: String,
    @SerializedName("sortOrder") val sortOrder: Int
)

data class UpdateAccentColorActiveRequest(
    @SerializedName("active") val active: Boolean
)

data class ThemeGridSizeRequest(
    @SerializedName("gridSize") val gridSize: Int
)
