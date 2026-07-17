package com.mefy.platemate.data.remote.dto.admin

data class PlateRemovalReasonAdminDto(
    val id: Long,
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val sortOrder: Int,
    val active: Boolean,
    val createdAt: String?,
    val updatedAt: String?
)
