package com.mefy.platemate.domain.model.admin

data class ReportTypeAdmin(
    val id: Long,
    val code: String,
    val label: String,
    val description: String,
    val iconKey: String,
    val severityCode: String,
    val colorHex: String,
    val weight: Int,
    val sortOrder: Int,
    val active: Boolean
)

/** Editable payload shared by add and update. */
data class ReportTypeInput(
    val code: String,
    val label: String,
    val description: String,
    val iconKey: String,
    val severityCode: String,
    val colorHex: String,
    val weight: Int,
    val sortOrder: Int
)
