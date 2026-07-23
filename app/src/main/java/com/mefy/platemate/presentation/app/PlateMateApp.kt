package com.mefy.platemate.presentation.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.app.components.AppShell
import com.mefy.platemate.presentation.app.effects.AppAuthEffects
import com.mefy.platemate.presentation.app.effects.AppJankEffects
import com.mefy.platemate.presentation.app.effects.AppNotificationEffects
import com.mefy.platemate.presentation.app.effects.AppUiEffects
import com.mefy.platemate.presentation.app.state.AppState
import com.mefy.platemate.presentation.app.state.rememberAppState
import com.mefy.platemate.presentation.app.viewmodel.AppViewModel
import com.mefy.platemate.presentation.common.banner.PMInAppNotificationBanner
import com.mefy.platemate.presentation.common.banner.bannerFor
import com.mefy.platemate.presentation.common.dialog.rememberDialogHostState
import com.mefy.platemate.presentation.common.ext.findActivity
import com.mefy.platemate.presentation.common.messaging.UiMessageHandlers
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.navigation.AppNavHost
import com.mefy.platemate.presentation.performance.StartupJankMonitor
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
fun PlateMateApp(
    appState: AppState = rememberAppState(),
    appViewModel: AppViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val spacing = PMTheme.spacing
    val dialogHostState = rememberDialogHostState()
    
    val startupJankMonitor = remember(context) {
        context.findActivity()?.window?.let(StartupJankMonitor::createOrNull)
    }

    val networkState by appViewModel.isOnline.collectAsStateWithLifecycle()

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

    AppAuthEffects(navController = appState.navController)

    AppUiEffects(
        navController = appState.navController,
        dialogHostState = dialogHostState,
        viewModel = appViewModel
    )

    AppJankEffects(
        currentRoute = appState.currentDestination?.route,
        startupJankMonitor = startupJankMonitor
    )

    AppNotificationEffects(
        viewModel = appViewModel,
        bannerController = appState.bannerController,
        navController = appState.navController,
        currentTopLevelDestination = appState.currentTopLevelDestination
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AppShell(
            networkState = networkState,
            uiMessageHandlers = commonUiEventHandlers,
            dialogHostState = dialogHostState,
            showBottomBar = appState.showBottomBar,
            selectedTopLevelDestination = appState.currentTopLevelDestination,
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
                .padding(top = spacing.s8)
        )
    }
}
