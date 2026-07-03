package com.mefy.platemate.domain.model.notification

enum class NotificationType {
    SYSTEM,
    MESSAGE,
    FRIEND_REQUEST,
    PLATE_REVIEW,
    NEW_FOLLOWER;

    companion object {
        fun fromString(value: String?): NotificationType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SYSTEM
    }
}
