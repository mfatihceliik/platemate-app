package com.mefy.platemate.presentation.features.main.search.model

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel

@Immutable
data class SearchRecentUiModel(
    val normalizedPlateCode: String,
    val plateCode: String,
    val cityName: String?,
    val reportTags: List<PlateReportTagUiModel>,
    val ratingAverage: Double,
    val commentCount: Long,
    val isBookmarked: Boolean
)
