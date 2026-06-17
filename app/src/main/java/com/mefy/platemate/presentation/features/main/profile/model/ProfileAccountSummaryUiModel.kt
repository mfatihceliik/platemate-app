package com.mefy.platemate.presentation.features.main.profile.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileAccountSummaryUiModel(
    val email: String = "",
    val joinedAtText: String = "-",
    val premiumUntilText: String? = null,
    val isPremiumActive: Boolean = false
)
