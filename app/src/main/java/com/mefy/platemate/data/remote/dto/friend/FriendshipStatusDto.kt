package com.mefy.platemate.data.remote.dto.friend

import com.mefy.platemate.domain.model.social.FriendshipStatus

enum class FriendshipStatusDto(val wireValue: String) {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    UNKNOWN("UNKNOWN");

    fun toDomain(): FriendshipStatus = when (this) {
        PENDING -> FriendshipStatus.PENDING
        ACCEPTED -> FriendshipStatus.ACCEPTED
        REJECTED -> FriendshipStatus.REJECTED
        UNKNOWN -> FriendshipStatus.UNKNOWN
    }

    companion object {
        fun fromWire(value: String?): FriendshipStatusDto {
            val normalized = value?.trim().orEmpty()
            return entries.firstOrNull { it.wireValue.equals(normalized, ignoreCase = true) } ?: UNKNOWN
        }
    }
}