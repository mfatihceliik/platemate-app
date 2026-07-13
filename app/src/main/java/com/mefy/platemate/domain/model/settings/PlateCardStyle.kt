package com.mefy.platemate.domain.model.settings

enum class PlateCardStyle {
    CLASSIC,
    SPOTLIGHT,
    MINIMAL;

    companion object {
        fun fromName(raw: String?): PlateCardStyle {
            return entries.firstOrNull { it.name == raw } ?: CLASSIC
        }
    }
}
