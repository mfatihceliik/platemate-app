package com.mefy.platemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
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

    @Inject
    lateinit var themePreferenceStore: ThemePreferenceStore

    @Inject
    lateinit var languagePreferenceStore: LanguagePreferenceStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themePreferenceStore.observeThemeMode().collectAsState(initial = AppThemeMode.SYSTEM)
            val language by languagePreferenceStore.observeLanguage().collectAsState(initial = AppLanguage.TR)
            val accentColorArgb by themePreferenceStore.observeAccentColorArgb()
                .collectAsState(initial = themePreferenceStore.peekAccentColorArgb())

            val darkTheme = when (themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }
            val accentColor = Color(accentColorArgb.toInt())

            val currentLang = language ?: AppLanguage.TR
            val locale = Locale.forLanguageTag(currentLang.headerValue)
            
            val context = LocalContext.current
            val newConfig = android.content.res.Configuration(LocalConfiguration.current)
            
            if (newConfig.locales[0].toLanguageTag() != locale.toLanguageTag()) {
                Locale.setDefault(locale)
                newConfig.setLocale(locale)
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(newConfig, context.resources.displayMetrics)
            }
            
            val layoutDirection = if (android.text.TextUtils.getLayoutDirectionFromLocale(locale) == android.view.View.LAYOUT_DIRECTION_RTL) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            CompositionLocalProvider(
                LocalConfiguration provides newConfig,
                LocalLayoutDirection provides layoutDirection
            ) {
                PlateMateTheme(darkTheme = darkTheme, accentColor = accentColor) {
                    AppNavHost()
                }
            }
        }
    }
}
