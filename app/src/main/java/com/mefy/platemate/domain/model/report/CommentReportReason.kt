package com.mefy.platemate.domain.model.report

data class CommentReportReason(
    val code: String,
    val label: String,
    val requiresDescription: Boolean
)
