package com.mefy.platemate.presentation.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.mefy.platemate.presentation.common.dialog.DialogFactory
import com.mefy.platemate.presentation.common.dialog.DialogHostState
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.navigation.AuthGraphDestination
import com.mefy.platemate.presentation.navigation.navigateToAuthAndClearBackStack
import com.mefy.platemate.presentation.performance.StartupJankMonitor
import kotlinx.coroutines.flow.Flow

/**
 * Uygulamayı bir bütün olarak ilgilendiren (app-singleton) yan etkiler. Tek bir yerde,
 * kök kompozisyonda bir kez çalışır; ekran ağacına TAŞINMAZ (per-screen olursa çoklu
 * collector ve tekrarlı navigasyon oluşur).
 *
 * - Global UI olayları (oturum süresi dolması, global dialog)
 * - Kimlik doğrulama kaybında Auth grafiğine yönlendirme
 * - Açılış jank (StartupJankMonitor) rota güncellemesi + temizlik
 *
 * [com.mefy.platemate.presentation.navigation.AppNavHost] içinde bir kez çağrılır.
 */
@Composable
internal fun AppGlobalEffects(
    globalUiEvents: Flow<GlobalAppEvent>,
    isAuthenticated: Boolean?,
    currentRoute: String?,
    navController: NavHostController,
    dialogHostState: DialogHostState,
    startupJankMonitor: StartupJankMonitor?,
) {
    // Tüm uygulamayı bloklaması gereken kritik olaylar (oturum bitti) tek noktada burada gösterilir.
    LaunchedEffect(Unit) {
        globalUiEvents.collect { event ->
            when (event) {
                GlobalAppEvent.SessionExpired -> dialogHostState.showDialog(
                    DialogFactory.sessionExpiredDialog(
                        onConfirm = { navController.navigateToAuthAndClearBackStack() }
                    )
                )
            }
        }
    }

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated == false) {
            val destination = navController.currentDestination
            val route = destination?.route
            val isAlreadyInAuth = route == AuthGraphDestination::class.qualifiedName ||
                    destination?.hierarchy?.any { it.hasRoute(AuthGraphDestination::class) } == true

            if (!isAlreadyInAuth) {
                navController.navigateToAuthAndClearBackStack()
            }
        }
    }

    LaunchedEffect(currentRoute, startupJankMonitor) {
        startupJankMonitor?.updateCurrentRoute(currentRoute)
    }

    DisposableEffect(startupJankMonitor) {
        onDispose {
            startupJankMonitor?.stop()
        }
    }
}
