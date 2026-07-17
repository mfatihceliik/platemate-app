package com.mefy.platemate.domain.model.admin

data class PlateRemovalReasonAdmin(
    val id: Long,
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val sortOrder: Int,
    val active: Boolean,
    val createdAt: String?,
    val updatedAt: String?
)
