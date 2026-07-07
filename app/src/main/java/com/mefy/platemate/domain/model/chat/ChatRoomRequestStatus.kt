package com.mefy.platemate.domain.model.chat

enum class ChatRoomRequestStatus {
    PENDING,
    ACCEPTED,
    DECLINED;

    companion object {
        fun fromString(value: String?): ChatRoomRequestStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: ACCEPTED
    }
}
