package com.mefy.platemate.domain.model.discovery

enum class DiscoveryFeedType {
    FREE,
    PREMIUM;

    companion object {
        fun fromString(raw: String?): DiscoveryFeedType {
            return entries.firstOrNull { it.name == raw } ?: FREE
        }
    }
}
