package com.mefy.platemate.data.local.room.model

data class RecentSearchReportTypeLocal(
    val code: String,
    val label: String,
    val description: String,
    val iconKey: String,
    val severity: String,
    val colorHex: String,
    val weight: Int,
    val sortOrder: Int
)
