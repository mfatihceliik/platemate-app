package com.mefy.platemate.domain.model.profile

enum class ProfileFriendshipStatus {
    NONE,
    PENDING_SENT,
    PENDING_RECEIVED,
    FRIENDS,
    UNKNOWN;

    companion object
}

fun ProfileFriendshipStatus.Companion.fromWire(raw: String?): ProfileFriendshipStatus = when (raw) {
    "NONE" -> ProfileFriendshipStatus.NONE
    "PENDING_SENT" -> ProfileFriendshipStatus.PENDING_SENT
    "PENDING_RECEIVED" -> ProfileFriendshipStatus.PENDING_RECEIVED
    "FRIENDS" -> ProfileFriendshipStatus.FRIENDS
    else -> ProfileFriendshipStatus.UNKNOWN
}
