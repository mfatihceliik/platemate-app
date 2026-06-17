package com.mefy.platemate.domain.model.theme

enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK;
    
    companion object {
        fun fromName(name: String?): AppThemeMode {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: SYSTEM
        }
    }
}
