package com.mefy.platemate.domain.model.settings

data class UserSettings(
    val messagingEnabled: Boolean,
    val locationSharingEnabled: Boolean,
    val messageNotificationsEnabled: Boolean,
    val friendNotificationsEnabled: Boolean
)
