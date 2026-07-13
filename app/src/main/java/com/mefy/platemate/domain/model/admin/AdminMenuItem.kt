package com.mefy.platemate.domain.model.admin

data class AdminMenuItem(
    val code: String,
    val title: String,
    val iconKey: String,
    val sortOrder: Int,
    val badgeCount: Long?
)
