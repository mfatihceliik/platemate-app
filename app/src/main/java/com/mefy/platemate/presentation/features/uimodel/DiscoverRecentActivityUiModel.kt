package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType

@Immutable
data class DiscoverRecentActivityUiModel(
    val id: String,
    val type: RecentActivityActionType,
    val actorName: String,
    val actionText: String,
    val plateCode: String,
    val timeAgoText: String
)
