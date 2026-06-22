package com.mefy.platemate.domain.model.review

data class ReviewResponse(
    val reviewId: Long,
    val plateCode: String,
    val rating: Int,
    val comment: String,
    val status: String,
    val userId: Long,
    val username: String,
    val displayName: String?,
    val profilePhotoUrl: String?,
    val reportTypeCodes: List<String>,
    val createdAt: String?,
    val updatedAt: String?
)
