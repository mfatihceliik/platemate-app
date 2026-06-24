package com.mefy.platemate.domain.model.plate

data class PlateTagSummary(
    val code: String,
    val label: String,
    val iconKey: String,
    val colorHex: String,
    val count: Long
)
