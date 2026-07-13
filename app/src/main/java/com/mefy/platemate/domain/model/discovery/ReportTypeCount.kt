package com.mefy.platemate.domain.model.discovery

data class ReportTypeCount(
    val code: String,
    val label: String,
    val colorHex: String,
    val iconKey: String,
    val count: Long
)
