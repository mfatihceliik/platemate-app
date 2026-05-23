package com.mefy.platemate.data.remote.dto.settings

import com.google.gson.annotations.SerializedName

data class UpdateSettingsRequest(
    @SerializedName("messagingEnabled") val messagingEnabled: Boolean?,
    @SerializedName("locationSharingEnabled") val locationSharingEnabled: Boolean?,
    @SerializedName("messageNotificationsEnabled") val messageNotificationsEnabled: Boolean?,
    @SerializedName("friendNotificationsEnabled") val friendNotificationsEnabled: Boolean?
)