package com.mefy.platemate.domain.model.discovery

data class RecentActivity(
    val username: String,
    val plateCode: String,
    val actionType: RecentActivityActionType,
    val occurredAt: String,
    val rating: Double,
    val comment: String,
    val reportTypeCode: String,
    val reportTypeLabel: String
)
