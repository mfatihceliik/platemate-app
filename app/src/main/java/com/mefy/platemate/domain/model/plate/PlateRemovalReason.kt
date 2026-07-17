package com.mefy.platemate.domain.model.plate

data class PlateRemovalReason(
    val id: Long,
    val code: String,
    val label: String,
    val requiresDescription: Boolean
)
