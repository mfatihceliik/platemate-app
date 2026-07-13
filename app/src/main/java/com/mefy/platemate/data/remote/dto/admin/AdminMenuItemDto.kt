package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class AdminMenuItemDto(
    @SerializedName("code") val code: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("iconKey") val iconKey: String?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("badgeCount") val badgeCount: Long?
)
