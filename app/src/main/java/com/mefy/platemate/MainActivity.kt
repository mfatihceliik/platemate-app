package com.mefy.platemate

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.data.local.LanguagePreferenceStore
import com.mefy.platemate.data.local.ThemePreferenceStore
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.presentation.navigation.AppNavHost
import com.mefy.platemate.presentation.theme.PlateMateTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var themePreferenceStore: ThemePreferenceStore
    @Inject lateinit var languagePreferenceStore: LanguagePreferenceStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                when (TextUtils.getLayoutDirectionFromLocale(locale)) {
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
                    AppNavHost()
                }
            }
        }
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
