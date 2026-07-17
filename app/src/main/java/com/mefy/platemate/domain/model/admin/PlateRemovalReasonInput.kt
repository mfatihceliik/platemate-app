package com.mefy.platemate.domain.model.admin

data class PlateRemovalReasonInput(
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val sortOrder: Int
)
