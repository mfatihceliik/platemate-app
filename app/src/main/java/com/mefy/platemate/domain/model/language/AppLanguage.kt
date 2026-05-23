package com.mefy.platemate.domain.model.language

enum class AppLanguage(val headerValue: String) {
    TR("tr"),
    EN("en");

    companion object {
        fun fromHeader(value: String?): AppLanguage? {
            val normalized = value?.trim().orEmpty()
            return entries.firstOrNull { it.headerValue.equals(normalized, ignoreCase = true) }
        }
    }
}
