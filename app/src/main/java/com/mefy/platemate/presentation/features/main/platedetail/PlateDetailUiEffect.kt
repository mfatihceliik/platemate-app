package com.mefy.platemate.presentation.features.main.platedetail

sealed interface PlateDetailUiEffect {
    data object NavigateBack : PlateDetailUiEffect
    data class NavigateToReview(val plateCode: String) : PlateDetailUiEffect
    data class NavigateToUserProfile(val userId: Long) : PlateDetailUiEffect
    data class NavigateToActions(val plateCode: String) : PlateDetailUiEffect
    data class NavigateToEditReview(
        val plateCode: String,
        val reviewId: Long,
        val rating: Int,
        val comment: String
    ) : PlateDetailUiEffect
}
