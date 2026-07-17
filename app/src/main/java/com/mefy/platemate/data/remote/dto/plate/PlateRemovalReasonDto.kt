package com.mefy.platemate.data.remote.dto.plate

data class PlateRemovalReasonDto(
    val id: Long,
    val code: String,
    val label: String,
    val requiresDescription: Boolean
)
