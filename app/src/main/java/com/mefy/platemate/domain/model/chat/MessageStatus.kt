package com.mefy.platemate.domain.model.chat

enum class MessageStatus {
    SENT,
    DELIVERED,
    READ;

    companion object {
        fun fromString(value: String?): MessageStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SENT
    }
}
