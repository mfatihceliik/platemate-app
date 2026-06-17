package com.mefy.platemate.presentation.features.main.platedetail

sealed interface PlateDetailUiEffect {
    data object NavigateBack : PlateDetailUiEffect
    data class NavigateToReview(val plateCode: String) : PlateDetailUiEffect
}
