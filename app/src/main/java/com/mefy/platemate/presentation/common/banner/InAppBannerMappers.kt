package com.mefy.platemate.presentation.common.banner

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.domain.model.notification.NotificationType

fun AppNotification.toBanner(
    onClick: (() -> Unit)?
): InAppBannerUiModel = InAppBannerUiModel(
    title = bannerTitle(),
    message = bannerBody(),
    icon = type.bannerIcon(),
    severity = BannerSeverity.Info,
    onClick = onClick
)
fun bannerFor(
    message: String,
    severity: BannerSeverity
): InAppBannerUiModel = InAppBannerUiModel(
    title = null,
    message = message,
    icon = severity.bannerIcon(),
    severity = severity,
    onClick = null
)
private fun AppNotification.bannerTitle(): String = when (this) {
    is AppNotification.Message -> senderName
    is AppNotification.FriendRequest -> title.orEmpty()
    is AppNotification.PlateReview -> title.orEmpty()
    is AppNotification.NewFollower -> title.orEmpty()
    is AppNotification.System -> title.orEmpty()
}
private fun AppNotification.bannerBody(): String = when (this) {
    is AppNotification.Message -> body
    is AppNotification.FriendRequest -> content.orEmpty()
    is AppNotification.PlateReview -> content.orEmpty()
    is AppNotification.NewFollower -> content.orEmpty()
    is AppNotification.System -> content.orEmpty()
}
private fun NotificationType.bannerIcon(): ImageVector = when (this) {
    NotificationType.MESSAGE -> Icons.Filled.Email
    NotificationType.FRIEND_REQUEST -> Icons.Filled.PersonAdd
    NotificationType.PLATE_REVIEW -> Icons.Filled.Star
    NotificationType.NEW_FOLLOWER -> Icons.Filled.Person
    NotificationType.SYSTEM -> Icons.Filled.Notifications
}
private fun BannerSeverity.bannerIcon(): ImageVector = when (this) {
    BannerSeverity.Error -> Icons.Filled.ErrorOutline
    BannerSeverity.Success -> Icons.Filled.CheckCircle
    BannerSeverity.Info -> Icons.Filled.Info
}
