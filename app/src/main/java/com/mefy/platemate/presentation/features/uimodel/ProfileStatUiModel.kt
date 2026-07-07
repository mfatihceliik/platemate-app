package com.mefy.platemate.presentation.features.uimodel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class ProfileStatUiModel(
    val valueText: String,
    @param:StringRes val labelResId: Int
)
