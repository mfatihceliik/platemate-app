package com.mefy.platemate.domain.model.plate

data class RatingDistribution(
    val rating: Int,
    val count: Long,
    val percentage: Double
)
