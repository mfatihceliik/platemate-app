package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.app.state.AppState
import com.mefy.platemate.presentation.navigation.graphs.authGraph
import com.mefy.platemate.presentation.navigation.graphs.mainGraph
import com.mefy.platemate.presentation.navigation.graphs.splashGraph

@Composable
fun AppNavHost(
    appState: AppState
) {
    val navController = appState.navController

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = SplashDestination,
            enterTransition = { appEnter() },
            exitTransition = { appExit() },
            popEnterTransition = { appPopEnter() },
            popExitTransition = { appPopExit() }
        ) {
            splashGraph(
                onNavigateToAuth = navController::navigateToAuthGraphFromGate,
                onNavigateToMain = navController::navigateToMainGraphFromGate
            )

            authGraph(
                onNavigateAfterLogin = navController::navigateToMainAndClearBackStack,
                onNavigateAfterRegister = navController::navigateToMainAndClearBackStack,
                onNavigateToRegister = navController::navigateToRegister,
                onNavigateToLogin = navController::navigateToLogin
            )

            mainGraph(
                navController = navController,
                onNavigateToSearchDetail = navController::navigateToSearchDetail,
                onNavigateToDiscoverDetail = navController::navigateToDiscoverDetail,
                onShowSnackbar = { message -> appState.showMessage(message) },
            )
        }
    }
}