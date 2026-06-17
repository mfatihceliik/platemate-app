package com.mefy.platemate.presentation.features.main.profile.model

import androidx.compose.runtime.Immutable

sealed interface ProfileActivityUiModel {
    val id: String
    val createdAtText: String
    val sortKey: String
}

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

@Immutable
data class FriendRequestNotificationItem(
    override val id: String,
    val friendUserId: Long,
    val username: String,
    val statusCode: String,
    override val createdAtText: String,
    override val sortKey: String
) : ProfileActivityUiModel
