package com.mefy.platemate.presentation.features.admin.socialplatforms

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class SocialPlatformsUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val items: List<SocialPlatformListItem> = emptyList(),
    val togglingId: Long? = null
)
