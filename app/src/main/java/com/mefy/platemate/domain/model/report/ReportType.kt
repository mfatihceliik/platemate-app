package com.mefy.platemate.domain.model.report

data class ReportType(
    val code: String,
    val label: String,
    val description: String,
    val iconKey: String,
    val severity: String,
    val colorHex: String,
    val weight: Int,
    val sortOrder: Int
)
