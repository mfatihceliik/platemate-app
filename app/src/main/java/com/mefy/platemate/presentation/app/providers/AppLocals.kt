package com.mefy.platemate.presentation.app.providers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mefy.platemate.presentation.common.messaging.UiMessageHandlers

val LocalScaffoldPadding = compositionLocalOf { PaddingValues(0.dp) }
val LocalNetworkState = compositionLocalOf { true }

/**
 * Uygulamanın en tepe noktasından (AppShell) aşağıya dağıtılan UI Mesajı (Snackbar/Dialog)
 * işleyicilerini tutar. Ekranların lokal bildirimleri bu kök sink'e akar.
 */
val LocalUiMessageHandlers = staticCompositionLocalOf<UiMessageHandlers> {
    error("LocalUiMessageHandlers cannot provided; AppNavHost must be wrapped with CompositionLocalProvider.")
}

/**
 * Uygulamanın en tepe navigasyon kontrolcüsünü tutar. Ekranların, kendi içinden
 * (prop drilling yapmadan) geri gitmesini veya başka yerlere yönlenmesini sağlar.
 */
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("LocalNavController not provided")
}
