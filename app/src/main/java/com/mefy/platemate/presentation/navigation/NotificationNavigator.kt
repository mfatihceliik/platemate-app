package com.mefy.platemate.presentation.navigation

import androidx.navigation.NavHostController
import com.mefy.platemate.core.notification.model.AppNotification

object NotificationNavigator {

    /** Banner dokunması veya intent → tipe göre ilgili ekran. MESSAGE→konuşma, FRIEND_REQUEST→arkadaşlar vb. */
    fun handleInAppNotificationTap(
        notification: AppNotification,
        navController: NavHostController
    ) {
        when (notification) {
            is AppNotification.Message -> {
                val roomId = notification.roomId ?: return
                navController.navigate(
                    ChatDestination(
                        conversationId = roomId.toString(),
                        participantName = notification.senderName,
                    )
                )
            }
            is AppNotification.FriendRequest -> navController.navigateToProfileFriends()
            is AppNotification.PlateReview,
            is AppNotification.NewFollower,
            is AppNotification.System -> Unit
        }
    }
}
