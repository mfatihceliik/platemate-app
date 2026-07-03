package com.mefy.platemate.presentation.features.main.profile.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlateReviewNotificationItem(
    override val id: String,
    val normalizedPlateCode: String,
    val plateCode: String,
    val ratingAverage: Double,
    val commentCount: Long,
    val reviewStatus: ProfileReviewStatusUi,
    override val createdAtText: String,
    override val sortKey: String
) : ProfileActivityUiModel
