package com.mefy.platemate

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.google.firebase.analytics.FirebaseAnalytics
import com.mefy.platemate.core.startup.AppStartupInitializer
import com.mefy.platemate.presentation.performance.StartupTrace
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PlateMateApp : Application(), ImageLoaderFactory {

    @Inject lateinit var appStartupInitializer: AppStartupInitializer

    override fun onCreate() {
        super.onCreate()
        StartupTrace.markAppStart()
        configureAnalyticsCollection()
        appStartupInitializer.initialize()
    }

    // Social platform icons are served as SVG (e.g. Simple Icons CDN); the default
    // Coil ImageLoader has no SVG decoder registered.
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .build()

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
