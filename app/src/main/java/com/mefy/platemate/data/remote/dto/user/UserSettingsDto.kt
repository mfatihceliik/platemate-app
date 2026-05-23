package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserSettingsDto(
    @SerializedName("messagingEnabled") val messagingEnabled: Boolean,
    @SerializedName("locationSharingEnabled") val locationSharingEnabled: Boolean,
    @SerializedName("messageNotificationsEnabled") val messageNotificationsEnabled: Boolean,
    @SerializedName("friendNotificationsEnabled") val friendNotificationsEnabled: Boolean
)
