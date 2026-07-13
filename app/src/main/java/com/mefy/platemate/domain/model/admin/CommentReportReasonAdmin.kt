package com.mefy.platemate.domain.model.admin

data class CommentReportReasonAdmin(
    val id: Long,
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val sortOrder: Int,
    val active: Boolean
)

/** Editable payload shared by add and update. */
data class CommentReportReasonInput(
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val sortOrder: Int
)
