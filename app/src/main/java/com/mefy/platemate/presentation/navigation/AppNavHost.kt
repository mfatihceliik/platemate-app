package com.mefy.platemate.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.mefy.platemate.presentation.common.connectivity.OfflineBanner
import com.mefy.platemate.presentation.common.dialog.DialogFactory
import com.mefy.platemate.presentation.common.dialog.DialogHost
import com.mefy.platemate.presentation.common.messaging.UiMessageHandlers
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.common.messaging.LocalUiMessageHandlers
import com.mefy.platemate.presentation.common.dialog.rememberDialogHostState
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.LocalIsOnline
import com.mefy.platemate.presentation.components.LocalScaffoldPadding
import com.mefy.platemate.presentation.common.bottombar.MainBottomBar
import com.mefy.platemate.presentation.performance.StartupJankMonitor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mefy.platemate.MainActivityViewModel
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    appState: AppState = rememberAppState(),
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val currentDestination = appState.currentDestination
    val currentTopLevelDestination = appState.currentTopLevelDestination
    val shouldShowBottomBar = appState.shouldShowBottomBar
    val context = LocalContext.current
    val dialogHostState = rememberDialogHostState()
    val startupJankMonitor = remember(context) {
        context.findActivity()?.window?.let(StartupJankMonitor::createOrNull)
    }

    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    // Ekran-yerel UI olayları (snackbar/dialog) için tek kök sink. Tüm ekranlar
    // HandleUiMessages ile bu işleyicilere bağlanır.
    val commonUiEventHandlers = remember(appState, dialogHostState, context) {
        UiMessageHandlers(
            onShowSnackbar = { uiText -> appState.showSnackbar(uiText.resolve(context)) },
            onShowDialog = dialogHostState::showDialog
        )
    }

    // Tüm uygulamayı ilgilendiren kritik olaylar tek noktada burada gösterilir.
    LaunchedEffect(Unit) {
        viewModel.globalUiEvents.collect { event ->
            when (event) {
                is GlobalAppEvent.ShowGlobalDialog -> dialogHostState.showDialog(event.dialog)
                GlobalAppEvent.SessionExpired -> dialogHostState.showDialog(
                    DialogFactory.sessionExpiredDialog(
                        onConfirm = { appState.navController.navigateToAuthAndClearBackStack() }
                    )
                )
            }
        }
    }

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated == false) {
            val destination = appState.navController.currentDestination
            val currentRoute = destination?.route
            val isAlreadyInAuth = currentRoute == AuthGraphDestination::class.qualifiedName ||
                                  destination?.hierarchy?.any { it.hasRoute(AuthGraphDestination::class) } == true
            
            if (!isAlreadyInAuth) {
                appState.navController.navigateToAuthAndClearBackStack()
            }
        }
    }

    LaunchedEffect(currentDestination?.route, startupJankMonitor) {
        startupJankMonitor?.updateCurrentRoute(currentDestination?.route)
    }

    DisposableEffect(startupJankMonitor) {
        onDispose {
            startupJankMonitor?.stop()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        // Inset yönetimi alt bileşenlere devredilir (PMTopBar status-bar, MainBottomBar nav-bar);
        // içerik edge-to-edge çizebilir.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) }
    ) { innerPadding ->
        // MainBottomBar artık Scaffold slotu değil; içeriğin üstüne overlay edilir
        // (Box). Böylece ekran zemini bar bölgesinde sürekli olur ve floating pill
        // etrafında opak blok kalmaz. Ekranlar LocalScaffoldPadding ile pill üstüne
        // kadar boşluk bırakır.
        val showBottomBar = shouldShowBottomBar && currentTopLevelDestination != null
        val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val dims = MaterialTheme.pmDimensions
        val scaffoldPadding = if (showBottomBar) {
            PaddingValues(bottom = dims.sizing.mainBottomBarHeight + navBottom)
        } else {
            PaddingValues(0.dp)
        }

        CompositionLocalProvider(
            LocalScaffoldPadding provides scaffoldPadding,
            LocalIsOnline provides isOnline,
            LocalUiMessageHandlers provides commonUiEventHandlers
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    OfflineBanner(visible = !isOnline)
                    NavHost(
                        navController = appState.navController,
                        startDestination = SessionGateDestination,
                        modifier = Modifier.weight(1f)
                    ) {
                        sessionGateGraph(
                            onNavigateToAuth = appState.navController::navigateToAuthGraphFromGate,
                            onNavigateToMain = appState.navController::navigateToMainGraphFromGate
                        )

                        authGraph(
                            onNavigateAfterLogin = appState.navController::navigateToMainAndClearBackStack,
                            onNavigateAfterRegister = appState.navController::navigateToMainAndClearBackStack,
                            onNavigateToRegister = appState.navController::navigateToRegister,
                            onNavigateToLogin = appState.navController::navigateToLogin,
                            onShowSnackbar = { uiText -> appState.showSnackbar(uiText.resolve(context)) },
                            onShowDialog = dialogHostState::showDialog,
                            onBackClick = { appState.navController.popBackStack() },
                            modifier = Modifier.fillMaxSize()
                        )

                        mainGraph(
                            navController = appState.navController,
                            onNavigateToSearchDetail = appState.navController::navigateToSearchDetail,
                            onNavigateToDiscoverDetail = appState.navController::navigateToDiscoverDetail,
                            onShowSnackbar = { message -> appState.showSnackbar(message) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                if (showBottomBar) {
                    MainBottomBar(
                        selectedDestination = currentTopLevelDestination,
                        onDestinationSelected = appState::navigateToTopLevelDestination,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
            DialogHost(state = dialogHostState)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
