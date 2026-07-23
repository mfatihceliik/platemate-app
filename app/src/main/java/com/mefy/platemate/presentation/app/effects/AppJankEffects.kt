package com.mefy.platemate.presentation.app.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.mefy.platemate.presentation.performance.StartupJankMonitor

@Composable
internal fun AppJankEffects(
    currentRoute: String?,
    startupJankMonitor: StartupJankMonitor?
) {
    LaunchedEffect(currentRoute, startupJankMonitor) {
        startupJankMonitor?.updateCurrentRoute(currentRoute)
    }

    DisposableEffect(startupJankMonitor) {
        onDispose {
            startupJankMonitor?.stop()
        }
    }
}
