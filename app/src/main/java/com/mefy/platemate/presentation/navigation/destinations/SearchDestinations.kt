package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SearchDestination : AppDestination

@Serializable
data class SearchDetailDestination(
    val id: String,
    val highlightReviewId: Long = -1L
) : AppDestination

@Serializable
data class ReviewDestination(
    val plateCode: String,
    val reviewId: Long = -1L
) : AppDestination

@Serializable
data class PlateActionsDestination(
    val plateCode: String
) : AppDestination

@Serializable
data class PlateRemovalRequestDestination(
    val plateId: Long,
    val plateCode: String
) : AppDestination

@Serializable
data object CameraScannerDestination : AppDestination
