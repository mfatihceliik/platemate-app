package com.mefy.platemate.domain.model.auth

enum class UserRole {
    NORMAL,
    PREMIUM,
    ADMIN;

    companion object {
        fun fromString(value: String?): UserRole =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: NORMAL
    }
}
