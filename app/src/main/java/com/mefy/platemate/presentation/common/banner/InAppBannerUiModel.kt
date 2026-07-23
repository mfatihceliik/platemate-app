package com.mefy.platemate.presentation.common.banner

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
@Immutable
data class InAppBannerUiModel(
    val title: String?,
    val message: String,
    val icon: ImageVector,
    val severity: BannerSeverity,
    val onClick: (() -> Unit)? = null
)
