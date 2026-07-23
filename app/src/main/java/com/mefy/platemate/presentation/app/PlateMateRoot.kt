package com.mefy.platemate.presentation.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.app.providers.ProvideLocale
import com.mefy.platemate.presentation.app.providers.ProvideSystemBars
import com.mefy.platemate.presentation.app.settings.AppSettings
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PlateMateRoot(settings: AppSettings) {
    ProvideLocale(language = settings.language) {
        ProvideSystemBars(themeMode = settings.themeMode) { darkTheme ->
            val accentColor = Color(settings.accentColor.toInt())

            PlateMateTheme(darkTheme = darkTheme, accentColor = accentColor) {
                PlateMateApp()
            }
        }
    }
}
