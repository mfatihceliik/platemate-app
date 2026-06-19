package com.mefy.platemate.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.mefy.platemate.presentation.common.event.CommonDialogHost
import com.mefy.platemate.presentation.common.event.rememberCommonDialogHostState
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.LocalScaffoldPadding
import com.mefy.platemate.presentation.features.main.MainBottomBar
import com.mefy.platemate.presentation.performance.StartupJankMonitor

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mefy.platemate.MainActivityViewModel

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
    val dialogHostState = rememberCommonDialogHostState()
    val startupJankMonitor = remember(context) {
        context.findActivity()?.window?.let(StartupJankMonitor::createOrNull)
    }

    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

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
        snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) },
        bottomBar = {
            if (shouldShowBottomBar && currentTopLevelDestination != null) {
                MainBottomBar(
                    selectedDestination = currentTopLevelDestination,
                    onDestinationSelected = appState::navigateToTopLevelDestination
                )
            }
        }
    ) { innerPadding ->
        // Scaffold padding'i NavHost'a uygulanmaz (edge-to-edge); CompositionLocal ile
        // PMBaseScreen'e akar ve orada içeriğe uygulanır.
        CompositionLocalProvider(LocalScaffoldPadding provides innerPadding) {
            NavHost(
                navController = appState.navController,
                startDestination = SessionGateDestination,
                modifier = Modifier.fillMaxSize()
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
        CommonDialogHost(state = dialogHostState)
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
