package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserSettingsDto(
    @SerializedName("messagingEnabled") val messagingEnabled: Boolean,
    @SerializedName("onlineVisibilityEnabled") val onlineVisibilityEnabled: Boolean = true,
    @SerializedName("messageNotificationsEnabled") val messageNotificationsEnabled: Boolean,
    @SerializedName("friendNotificationsEnabled") val friendNotificationsEnabled: Boolean,
    @SerializedName("plateReviewNotificationsEnabled") val plateReviewNotificationsEnabled: Boolean = false,
    @SerializedName("newFollowerNotificationsEnabled") val newFollowerNotificationsEnabled: Boolean = false,
    @SerializedName("reviewReplyNotificationsEnabled") val reviewReplyNotificationsEnabled: Boolean = false
)
