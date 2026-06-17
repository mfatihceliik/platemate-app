package com.mefy.platemate.domain.model.review

import com.mefy.platemate.domain.model.common.AppDateTime

data class Review(
    val id: Long,
    val plateCode: String,
    val rating: Int,
    val comment: String,
    val reviewStatus: String,
    val userId: Long,
    val reviewerUsername: String,
    val createdAt: AppDateTime?,
    val updatedAt: AppDateTime?
)
