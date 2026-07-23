package com.mefy.platemate.domain.model.settings

data class UserSettings(
    val messagingEnabled: Boolean,
    val onlineVisibilityEnabled: Boolean = true,
    val messageNotificationsEnabled: Boolean,
    val friendNotificationsEnabled: Boolean,
    val plateReviewNotificationsEnabled: Boolean = false,
    val newFollowerNotificationsEnabled: Boolean = false,
    val reviewReplyNotificationsEnabled: Boolean = false,
    val followingListVisible: Boolean = true
)
