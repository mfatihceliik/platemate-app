package com.mefy.platemate.presentation.features.main.platedetail

sealed interface PlateDetailUiAction {
    data object BackClicked : PlateDetailUiAction
    data object BookmarkClicked : PlateDetailUiAction
    data object ReviewClicked : PlateDetailUiAction
}
