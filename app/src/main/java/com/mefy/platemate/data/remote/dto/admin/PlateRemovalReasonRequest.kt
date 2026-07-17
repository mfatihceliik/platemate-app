package com.mefy.platemate.data.remote.dto.admin

data class PlateRemovalReasonRequest(
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val sortOrder: Int
)
