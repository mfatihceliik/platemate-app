package com.mefy.platemate.presentation.features.uimodel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class DiscoverMetricUiModel(
    val type: DiscoverMetricUiType,
    val valueText: String,
    @param:StringRes val labelResId: Int,
    @param:StringRes val periodResId: Int,
    val deltaText: String? = null,
    val deltaPositive: Boolean? = null
)
