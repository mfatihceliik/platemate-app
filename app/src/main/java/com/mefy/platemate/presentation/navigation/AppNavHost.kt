package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.mefy.platemate.presentation.app.AppState
import com.mefy.platemate.presentation.navigation.graphs.mainGraph
import com.mefy.platemate.presentation.navigation.graphs.splashGraph

/**
 * Yalnızca navigasyon: [NavHost] + grafikler + geçiş animasyonları. Uygulama chrome'u
 * (Scaffold, banner, alt bar), composition local sağlama ve app-singleton yan etkiler
 * burada DEĞİL ([AppShell] ve [PlateMateAppRoot]).
 *
 * Ekran-yerel geri-bildirimler [screenComposable] ile kaydedilen hedeflerde `LocalUiMessageHandlers`
 * üzerinden banner'a toplanır (sağlayıcı: [AppShell]); rotalar artık kendileri toplamaz.
 */
@Composable
fun AppNavHost(
    appState: AppState
) {
    val navController = appState.navController

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
            onNavigateToLogin = navController::navigateToLogin,
            onBackClick = { navController.popBackStack() },
        )

        mainGraph(
            navController = navController,
            onNavigateToSearchDetail = navController::navigateToSearchDetail,
            onNavigateToDiscoverDetail = navController::navigateToDiscoverDetail,
            onShowSnackbar = { message -> appState.showMessage(message) },
        )
    }
}