package com.mefy.platemate

import android.app.Application
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.mefy.platemate.presentation.performance.StartupTrace
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlateMateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        StartupTrace.markAppStart()
        configureAnalyticsCollection()
    }

    private fun configureAnalyticsCollection() {
        if (!BuildConfig.DEBUG) return

        FirebaseAnalytics.getInstance(this)
            .setAnalyticsCollectionEnabled(BuildConfig.ENABLE_FIREBASE_ANALYTICS_COLLECTION)

        Log.i(
            TAG,
            "firebase_analytics_collection_enabled=${BuildConfig.ENABLE_FIREBASE_ANALYTICS_COLLECTION}"
        )
    }

    private companion object {
        const val TAG = "PlateMateApp"
    }
}
