package com.mefy.platemate.domain.model.admin

data class CommentReport(
    val id: Long,
    val commentId: Long?,
    val plateCode: String,
    val reasonCode: String,
    val description: String,
    val statusCode: String,
    val createdAt: String?
)
