package com.mefy.platemate.data.remote.dto.settings

import com.google.gson.annotations.SerializedName

data class UpdateSettingsRequest(
    @SerializedName("messagingEnabled") val messagingEnabled: Boolean?,
    @SerializedName("onlineVisibilityEnabled") val onlineVisibilityEnabled: Boolean? = null,
    @SerializedName("messageNotificationsEnabled") val messageNotificationsEnabled: Boolean?,
    @SerializedName("friendNotificationsEnabled") val friendNotificationsEnabled: Boolean?,
    @SerializedName("plateReviewNotificationsEnabled") val plateReviewNotificationsEnabled: Boolean? = null,
    @SerializedName("newFollowerNotificationsEnabled") val newFollowerNotificationsEnabled: Boolean? = null,
    @SerializedName("reviewReplyNotificationsEnabled") val reviewReplyNotificationsEnabled: Boolean? = null
)
