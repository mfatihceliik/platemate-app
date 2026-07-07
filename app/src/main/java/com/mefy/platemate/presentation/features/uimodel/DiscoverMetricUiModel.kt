package com.mefy.platemate.presentation.features.uimodel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class DiscoverMetricUiModel(
    val type: DiscoverMetricUiType,
    val valueText: String,
    @StringRes val labelResId: Int,
    @StringRes val periodResId: Int
)
