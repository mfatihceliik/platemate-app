package com.mefy.platemate.presentation.features.main.profile.settings.notifications

import androidx.compose.runtime.Immutable

@Immutable
data class NotificationPreferencesUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val messagingEnabled: Boolean = false,
    val messageNotificationsEnabled: Boolean = false,
    val friendNotificationsEnabled: Boolean = false,
    val newFollowerEnabled: Boolean = false,
    val plateReviewEnabled: Boolean = false,
    val reviewReplyEnabled: Boolean = false,
    val initialMessagingEnabled: Boolean = false,
    val initialMessageNotificationsEnabled: Boolean = false,
    val initialFriendNotificationsEnabled: Boolean = false,
    val initialNewFollowerEnabled: Boolean = false,
    val initialPlateReviewEnabled: Boolean = false,
    val initialReviewReplyEnabled: Boolean = false,
    val hasChanges: Boolean = false
)
