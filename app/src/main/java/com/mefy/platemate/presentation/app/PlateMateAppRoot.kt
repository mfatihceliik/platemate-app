package com.mefy.platemate.presentation.app

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mefy.platemate.MainActivityViewModel
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.presentation.common.banner.bannerFor
import com.mefy.platemate.presentation.common.banner.toBanner
import com.mefy.platemate.presentation.common.dialog.rememberDialogHostState
import com.mefy.platemate.presentation.common.banner.PMInAppNotificationBanner
import com.mefy.platemate.presentation.common.messaging.UiMessageHandlers
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.navigation.AppNavHost
import com.mefy.platemate.presentation.navigation.ChatDestination
import com.mefy.platemate.presentation.navigation.isTopLevelStartDestination
import com.mefy.platemate.presentation.navigation.navigateToProfileFriends
import com.mefy.platemate.presentation.navigation.toTopLevelDestinationOrNull
import com.mefy.platemate.presentation.performance.StartupJankMonitor
import com.mefy.platemate.presentation.theme.PMDimensions
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Uygulamanın kök orchestrator'ı: app state + global yan etkiler + chrome ([AppShell]) +
 * navigasyon ([AppNavHost]) parçalarını birbirine bağlar. Tek sorumluluğu bu bağlama.
 * Chrome [AppShell]'de, navigasyon [AppNavHost]'ta, yan etkiler [AppGlobalEffects]'te. Tüm
 * geri-bildirim (hata/başarı/push) üstten inen tek banner'da ([appState.bannerController]).
 *
 * (İsim notu: `PlateMateApp` Hilt Application sınıfında kullanılıyor; kök composable
 * `PlateMateAppRoot`.)
 */
@Composable
fun PlateMateAppRoot(
    appState: AppState = rememberAppState(),
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dialogHostState = rememberDialogHostState()
    val startupJankMonitor = remember(context) {
        context.findActivity()?.window?.let(StartupJankMonitor::createOrNull)
    }
    val dims: PMDimensions = MaterialTheme.pmDimensions

    val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    // Back stack tek noktadan okunur; türevler ondan hesaplanır. (Önceden üç ayrı
    // currentBackStackEntryAsState aboneliği vardı → her navigasyonda üç kez tetiklenirdi.)
    val currentDestination = appState.currentDestination
    val currentTopLevelDestination = currentDestination.toTopLevelDestinationOrNull()
    val showBottomBar = (currentDestination.isTopLevelStartDestination() || 
                         currentDestination?.route?.contains("UserProfileDestination") == true) && 
                        currentTopLevelDestination != null

    // Ekran-yerel UI bildirimleri (hata/başarı) → üstten inen banner (tek kök sink).
    val commonUiEventHandlers = remember(appState, context) {
        UiMessageHandlers(
            onShowSnackbar = { uiText, severity ->
                appState.bannerController.show(bannerFor(uiText.resolve(context), severity))
            },
            onShowDialog = { dialog ->
                dialogHostState.showDialog(dialog)
            }
        )
    }

    AppGlobalEffects(
        globalUiEvents = viewModel.globalUiEvents,
        isAuthenticated = isAuthenticated,
        currentRoute = currentDestination?.route,
        navController = appState.navController,
        dialogHostState = dialogHostState,
        startupJankMonitor = startupJankMonitor,
    )

    // App AÇIKKEN gelen push bildirimi → banner (tıklayınca ilgili ekrana gider).
    LaunchedEffect(viewModel) {
        viewModel.inAppNotifications.collect { notification ->
            appState.bannerController.show(
                notification.toBanner(
                    onClick = { handleInAppNotificationTap(notification, appState.navController) }
                )
            )
        }
    }

    // Sistem bildirimine dokunma → banner ile aynı handler. Ana grafik hazır olunca (top-level
    // destination var) yönlendir; böylece cold-start'ta Splash çözülene kadar bekler.
    val notificationNavTarget by viewModel.notificationNavTarget.collectAsStateWithLifecycle()
    LaunchedEffect(notificationNavTarget, currentTopLevelDestination) {
        val target = notificationNavTarget
        if (target != null && currentTopLevelDestination != null) {
            handleInAppNotificationTap(target, appState.navController)
            viewModel.consumeNotificationNavTarget()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppShell(
            isOnline = isOnline,
            uiMessageHandlers = commonUiEventHandlers,
            dialogHostState = dialogHostState,
            showBottomBar = showBottomBar,
            selectedTopLevelDestination = currentTopLevelDestination,
            onTopLevelDestinationSelected = appState::navigateToTopLevelDestination,
        ) {
            AppNavHost(appState = appState)
        }

        PMInAppNotificationBanner(
            banner = appState.bannerController.current,
            resetKey = appState.bannerController.resetKey,
            onDismiss = { appState.bannerController.dismiss() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = dims.spacing.s8)
        )
    }
}

/** Banner dokunması → tipe göre ilgili ekran. MESSAGE→konuşma, FRIEND_REQUEST→arkadaşlar; diğerleri yalnız kapanır. */
private fun handleInAppNotificationTap(
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

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
