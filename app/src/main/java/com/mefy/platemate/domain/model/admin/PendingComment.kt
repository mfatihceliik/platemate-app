package com.mefy.platemate.domain.model.admin

data class PendingComment(
    val id: Long,
    val plateCode: String,
    val username: String,
    val rating: Int,
    val comment: String,
    val reportCount: Int,
    val reportTags: List<String>,
    val createdAt: String?
)
