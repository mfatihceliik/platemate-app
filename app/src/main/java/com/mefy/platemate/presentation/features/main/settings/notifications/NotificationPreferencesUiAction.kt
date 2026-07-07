package com.mefy.platemate.presentation.features.main.settings.notifications

sealed interface NotificationPreferencesUiAction {
    data class MessagingChanged(val enabled: Boolean) : NotificationPreferencesUiAction
    data class OnlineVisibilityChanged(val enabled: Boolean) : NotificationPreferencesUiAction
    data class MessageNotificationsChanged(val enabled: Boolean) : NotificationPreferencesUiAction
    data class FriendNotificationsChanged(val enabled: Boolean) : NotificationPreferencesUiAction
    data class NewFollowerChanged(val enabled: Boolean) : NotificationPreferencesUiAction
    data class PlateReviewChanged(val enabled: Boolean) : NotificationPreferencesUiAction
    data class ReviewReplyChanged(val enabled: Boolean) : NotificationPreferencesUiAction
    data object SaveClicked : NotificationPreferencesUiAction
    data object RetryClicked : NotificationPreferencesUiAction
}
