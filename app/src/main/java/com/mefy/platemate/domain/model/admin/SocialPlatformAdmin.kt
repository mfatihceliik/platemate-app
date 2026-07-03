package com.mefy.platemate.domain.model.admin

data class SocialPlatformAdmin(
    val id: Long,
    val code: String,
    val label: String,
    val iconUrl: String,
    val backgroundColorHex: String,
    val iconTintColorHex: String,
    val sortOrder: Int,
    val active: Boolean
)

/** Editable payload shared by add and update. */
data class SocialPlatformInput(
    val code: String,
    val label: String,
    val iconUrl: String,
    val backgroundColorHex: String,
    val iconTintColorHex: String,
    val sortOrder: Int
)
