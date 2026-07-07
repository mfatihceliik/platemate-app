package com.mefy.platemate.domain.model.admin

data class AccentColorAdmin(
    val id: Long,
    val hex: String,
    val sortOrder: Int,
    val active: Boolean
)

/** Editable payload shared by add and update. */
data class AccentColorInput(
    val hex: String,
    val sortOrder: Int
)
