package com.mefy.platemate.presentation.features.main.discover.uimodel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiType

@Immutable
data class DiscoverMetricUiModel(
    val type: DiscoverMetricUiType,
    val valueText: String,
    @StringRes val labelResId: Int,
    @StringRes val periodResId: Int
)
