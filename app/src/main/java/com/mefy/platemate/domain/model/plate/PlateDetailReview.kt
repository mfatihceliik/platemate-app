package com.mefy.platemate.domain.model.plate

data class PlateDetailReview(
    val id: Long,
    val userId: Long,
    val username: String,
    val displayName: String?,
    val profilePhotoUrl: String?,
    val rating: Int,
    val comment: String?,
    val reportTags: List<String>,
    val createdAt: String?
)
