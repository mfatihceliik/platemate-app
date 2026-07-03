package com.mefy.platemate.domain.model.admin

data class PlateRemovalRequest(
    val id: Long,
    val plateCode: String,
    val requesterUsername: String,
    val requesterEmail: String,
    val reasonCode: String,
    val description: String,
    val statusCode: String,
    val createdAt: String?
)
