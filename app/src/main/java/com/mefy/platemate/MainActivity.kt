package com.mefy.platemate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.core.notification.NotificationIntentFactory
import com.mefy.platemate.data.local.LanguagePreferenceStore
import com.mefy.platemate.data.local.ThemePreferenceStore
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.presentation.app.PlateMateRoot
import com.mefy.platemate.presentation.app.settings.AppSettings
import com.mefy.platemate.presentation.common.global.NotificationNavigationBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

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

        val settingsFlow = combine(
            themePreferenceStore.observeThemeMode(),
            languagePreferenceStore.observeLanguage(),
            themePreferenceStore.observeAccentColorArgb()
        ) { theme, lang, accent ->
            AppSettings(
                themeMode = theme,
                language = lang ?: AppLanguage.TR,
                accentColor = accent
            )
        }

        val initialSettings = AppSettings(
            themeMode = themePreferenceStore.peekThemeMode(),
            language = languagePreferenceStore.peekLanguageOrNull() ?: AppLanguage.TR,
            accentColor = themePreferenceStore.peekAccentColorArgb()
        )

        setContent {
            val settings by settingsFlow.collectAsStateWithLifecycle(initialValue = initialSettings)
            PlateMateRoot(settings = settings)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        notificationIntentFactory.parse(intent)?.let(notificationNavigationBus::post)
    }
}
