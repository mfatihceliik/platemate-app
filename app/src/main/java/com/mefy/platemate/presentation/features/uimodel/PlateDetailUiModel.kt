package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class PlateDetailUiModel(
    val id: String,
    val rank: Int? = null,
    val plateCode: String,
    val cityName: String?,
    val reportTags: List<PlateReportTagUiModel> = emptyList(),
    val ratingAverage: Double,
    val commentCount: Long
)
