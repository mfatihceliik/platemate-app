package com.mefy.platemate.presentation.features.main.platedetail.review

sealed interface ReviewUiEffect {
    data object NavigateBack : ReviewUiEffect
    data object ReviewSubmitted : ReviewUiEffect
}
