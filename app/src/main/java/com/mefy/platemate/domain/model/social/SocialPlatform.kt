package com.mefy.platemate.domain.model.social

data class SocialPlatform(
    val id: Long,
    val code: String,
    val label: String,
    val iconUrl: String?,
    val backgroundColorHex: String?,
    val iconTintColorHex: String?,
    val sortOrder: Int
)
