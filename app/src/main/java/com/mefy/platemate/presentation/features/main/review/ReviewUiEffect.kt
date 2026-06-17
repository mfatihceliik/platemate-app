package com.mefy.platemate.presentation.features.main.review

sealed interface ReviewUiEffect {
    data object NavigateBack : ReviewUiEffect
    data object ReviewSubmitted : ReviewUiEffect
}
