package com.mefy.platemate.domain.model.location

import com.mefy.platemate.domain.model.common.AppDateTime

data class UserLocation(
    val id: Long,
    val userId: Long,
    val username: String,
    val latitude: Double,
    val longitude: Double,
    val lastUpdatedAt: AppDateTime?
)
