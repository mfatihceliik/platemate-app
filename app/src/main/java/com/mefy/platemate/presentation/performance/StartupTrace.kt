package com.mefy.platemate.presentation.performance

import android.os.SystemClock
import android.util.Log
import com.mefy.platemate.BuildConfig
import java.util.concurrent.TimeUnit

object StartupTrace {

    private const val TAG = "StartupTrace"
    private const val NOT_MARKED = -1L

    @Volatile
    private var appStartNanos: Long = NOT_MARKED

    @Volatile
    private var searchRouteEnterNanos: Long = NOT_MARKED

    @Volatile
    private var searchFirstFrameNanos: Long = NOT_MARKED

    fun markAppStart() {
        if (!BuildConfig.ENABLE_STARTUP_JANK_MONITORING || appStartNanos != NOT_MARKED) return

        appStartNanos = SystemClock.elapsedRealtimeNanos()
        Log.i(TAG, "marker=app_start")
    }

    fun markSearchRouteEntered() {
        if (!BuildConfig.ENABLE_STARTUP_JANK_MONITORING || searchRouteEnterNanos != NOT_MARKED) return

        val nowNanos = SystemClock.elapsedRealtimeNanos()
        searchRouteEnterNanos = nowNanos
        ensureAppStart(nowNanos)
        Log.i(
            TAG,
            "marker=search_route_enter startup_to_search_route_ms=${elapsedFromAppStartMs(nowNanos)}"
        )
    }

    fun markSearchFirstFrameRendered() {
        if (!BuildConfig.ENABLE_STARTUP_JANK_MONITORING || searchFirstFrameNanos != NOT_MARKED) return

        val nowNanos = SystemClock.elapsedRealtimeNanos()
        searchFirstFrameNanos = nowNanos
        ensureAppStart(nowNanos)

        val searchToFirstFrameMs =
            if (searchRouteEnterNanos == NOT_MARKED) null
            else elapsedMs(searchRouteEnterNanos, nowNanos)

        Log.i(
            TAG,
            "marker=search_first_frame startup_to_search_first_frame_ms=${elapsedFromAppStartMs(nowNanos)} search_route_to_first_frame_ms=${searchToFirstFrameMs ?: "n/a"}"
        )
    }

    private fun ensureAppStart(nowNanos: Long) {
        if (appStartNanos == NOT_MARKED) {
            appStartNanos = nowNanos
            Log.i(TAG, "marker=app_start_inferred")
        }
    }

    private fun elapsedFromAppStartMs(nowNanos: Long): Long {
        if (appStartNanos == NOT_MARKED) return 0L
        return elapsedMs(appStartNanos, nowNanos)
    }

    private fun elapsedMs(startNanos: Long, endNanos: Long): Long {
        return TimeUnit.NANOSECONDS.toMillis(endNanos - startNanos)
    }
}

