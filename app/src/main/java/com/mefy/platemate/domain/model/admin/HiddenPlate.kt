package com.mefy.platemate.domain.model.admin

data class HiddenPlate(
    val id: Long,
    val plateCode: String,
    val statusCode: String,
    val hiddenReason: String,
    val reviewCount: Int,
    val reportCount: Int,
    val updatedAt: String?
)
