package com.mefy.platemate

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.core.notification.NotificationIntentFactory
import com.mefy.platemate.data.local.LanguagePreferenceStore
import com.mefy.platemate.data.local.ThemePreferenceStore
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.presentation.common.global.NotificationNavigationBus
import com.mefy.platemate.presentation.navigation.PlateMateAppRoot
import com.mefy.platemate.presentation.theme.PlateMateTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import androidx.core.text.layoutDirection

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var themePreferenceStore: ThemePreferenceStore
    @Inject lateinit var languagePreferenceStore: LanguagePreferenceStore
    @Inject lateinit var notificationIntentFactory: NotificationIntentFactory
    @Inject lateinit var notificationNavigationBus: NotificationNavigationBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleNotificationIntent(intent)
        setContent {
            val themeMode by themePreferenceStore.observeThemeMode()
                .collectAsStateWithLifecycle(initialValue = AppThemeMode.SYSTEM)
            val language by languagePreferenceStore.observeLanguage()
                .collectAsStateWithLifecycle(initialValue = AppLanguage.TR)
            val accentColorArgb by themePreferenceStore.observeAccentColorArgb()
                .collectAsStateWithLifecycle(initialValue = themePreferenceStore.peekAccentColorArgb())

            val darkTheme = when (themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }
            val accentColor = Color(accentColorArgb.toInt())

            // Edge-to-edge zaten aktif (enableEdgeToEdge). Sistem-bar ikon kontrastını
            // uygulama-içi tema değişimine bağla: açık temada koyu ikon, koyu temada açık.
            val view = LocalView.current
            SideEffect {
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }

            val currentLang = language ?: AppLanguage.TR
            val locale = remember(currentLang) {
                Locale.forLanguageTag(currentLang.headerValue)
            }

            val baseContext = LocalContext.current
            val configuration = LocalConfiguration.current

            // Localized context. ContextWrapper(activity) kullanıyoruz ki Hilt
            // LocalContext'ten Activity'yi unwrap edebilsin (createConfigurationContext
            // tek başına Activity olmayan bir ContextImpl döndürür ve hiltViewModel() çöker).
            val localizedContext = remember(currentLang, configuration) {
                val config = Configuration(configuration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
                LocaleContextWrapper(baseContext, config)
            }

            val layoutDirection = remember(currentLang) {
                when (locale.layoutDirection) {
                    View.LAYOUT_DIRECTION_RTL -> LayoutDirection.Rtl
                    else -> LayoutDirection.Ltr
                }
            }

            // Yan etki: JVM default locale'i senkron tut (formatlama, NumberFormat vs. için).
            SideEffect {
                Locale.setDefault(locale)
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides localizedContext.resources.configuration,
                LocalLayoutDirection provides layoutDirection
            ) {
                PlateMateTheme(darkTheme = darkTheme, accentColor = accentColor) {
                    PlateMateAppRoot()
                }
            }
        }
    }

    // App zaten açıkken (singleTop) bildirime dokunma buraya düşer.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    // Bildirim intent'ini çözüp nav-bus'a koyar; kök composable ana grafik hazırken yönlendirir.
    private fun handleNotificationIntent(intent: Intent?) {
        notificationIntentFactory.parse(intent)?.let(notificationNavigationBus::post)
    }
}

/**
 * Wraps the Activity context but overrides [getResources] with a locale-applied
 * Resources instance. Keeps the Activity reachable through the ContextWrapper
 * chain (required by Hilt's `hiltViewModel()`), while `stringResource` reads the
 * correct localized strings via `LocalContext.current.resources`.
 */
private class LocaleContextWrapper(
    base: Context,
    overrideConfiguration: Configuration
) : ContextWrapper(base) {

    private val localizedResources: Resources =
        base.createConfigurationContext(overrideConfiguration).resources

    override fun getResources(): Resources = localizedResources
}
