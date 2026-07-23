package com.mefy.platemate.domain.model.chat

enum class MessageStatus {
    // Local-only, never sent by the server: written before the network call so the message is
    // visible in Room the instant the user hits send, and flipped to FAILED in place on timeout.
    PENDING,
    FAILED,
    SENT,
    DELIVERED,
    READ,
    DELETED;

    companion object {
        fun fromString(value: String?): MessageStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SENT
    }
}
