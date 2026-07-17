package com.mefy.platemate.presentation.features.uimodel

enum class FriendRequestStatusUi {
    REQUESTED,
    ACCEPTED,
    REJECTED,
    REMOVED,
    UNKNOWN;

    companion object {
        fun from(rawStatus: String): FriendRequestStatusUi = when (rawStatus.trim().uppercase()) {
            "REQUESTED" -> REQUESTED
            "ACCEPTED" -> ACCEPTED
            "REJECTED" -> REJECTED
            "REMOVED" -> REMOVED
            else -> UNKNOWN
        }
    }
}
