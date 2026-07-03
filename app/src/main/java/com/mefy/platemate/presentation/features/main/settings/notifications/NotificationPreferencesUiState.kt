package com.mefy.platemate.presentation.features.main.settings.notifications

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class NotificationPreferencesUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val isSaving: Boolean = false,
    val messagingEnabled: Boolean = false,
    val onlineVisibilityEnabled: Boolean = false,
    val messageNotificationsEnabled: Boolean = false,
    val friendNotificationsEnabled: Boolean = false,
    val newFollowerEnabled: Boolean = false,
    val plateReviewEnabled: Boolean = false,
    val reviewReplyEnabled: Boolean = false,
    val initialMessagingEnabled: Boolean = false,
    val initialOnlineVisibilityEnabled: Boolean = false,
    val initialMessageNotificationsEnabled: Boolean = false,
    val initialFriendNotificationsEnabled: Boolean = false,
    val initialNewFollowerEnabled: Boolean = false,
    val initialPlateReviewEnabled: Boolean = false,
    val initialReviewReplyEnabled: Boolean = false,
    val hasChanges: Boolean = false
)
