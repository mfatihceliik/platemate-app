package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class DiscoverRecentActivityUiModel(
    val id: String,
    val type: RecentActivityActionType,
    val actorName: String,
    val actionText: UiText,
    val plateCode: String,
    val timeAgoText: UiText
)
