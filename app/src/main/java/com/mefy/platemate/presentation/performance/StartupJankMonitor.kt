package com.mefy.platemate.presentation.performance

import android.util.Log
import android.view.Window
import androidx.metrics.performance.JankStats
import com.mefy.platemate.BuildConfig
import java.util.concurrent.atomic.AtomicReference

class StartupJankMonitor private constructor(
    private val jankStats: JankStats,
    private val currentRoute: AtomicReference<String>
) {

    fun updateCurrentRoute(route: String?) {
        currentRoute.set(route ?: UNKNOWN_ROUTE)
    }

    fun stop() {
        jankStats.isTrackingEnabled = false
    }

    companion object {
        private const val TAG = "StartupJankMonitor"
        private const val UNKNOWN_ROUTE = "unknown"

        fun createOrNull(window: Window): StartupJankMonitor? {
            if (!BuildConfig.ENABLE_STARTUP_JANK_MONITORING) return null

            return runCatching {
                val routeHolder = AtomicReference(UNKNOWN_ROUTE)
                val stats = JankStats.createAndTrack(window) { frameData ->
                    if (frameData.isJank) {
                        Log.w(
                            TAG,
                            "jank_frame route=${routeHolder.get()} data=$frameData"
                        )
                    }
                }
                stats.isTrackingEnabled = true
                StartupJankMonitor(
                    jankStats = stats,
                    currentRoute = routeHolder
                )
            }.onFailure { throwable ->
                Log.w(TAG, "JankStats init failed: ${throwable.message}", throwable)
            }.getOrNull()
        }
    }
}

