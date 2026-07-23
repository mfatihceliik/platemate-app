package com.mefy.platemate.presentation.app.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mefy.platemate.presentation.app.viewmodel.AppViewModel
import com.mefy.platemate.presentation.common.banner.InAppBannerController
import com.mefy.platemate.presentation.common.banner.toBanner
import com.mefy.platemate.presentation.navigation.NotificationNavigator
import com.mefy.platemate.presentation.navigation.TopLevelDestination

@Composable
fun AppNotificationEffects(
    viewModel: AppViewModel,
    bannerController: InAppBannerController,
    navController: NavHostController,
    currentTopLevelDestination: TopLevelDestination?
) {
    LaunchedEffect(viewModel) {
        viewModel.inAppNotifications.collect { notification ->
            bannerController.show(
                notification.toBanner(
                    onClick = { NotificationNavigator.handleInAppNotificationTap(notification, navController) }
                )
            )
        }
    }

    val notificationNavTarget by viewModel.notificationNavTarget.collectAsStateWithLifecycle()

    LaunchedEffect(notificationNavTarget, currentTopLevelDestination) {
        val target = notificationNavTarget
        if (target != null && currentTopLevelDestination != null) {
            NotificationNavigator.handleInAppNotificationTap(target, navController)
            viewModel.consumeNotificationNavTarget()
        }
    }
}
