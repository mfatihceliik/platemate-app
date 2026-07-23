package com.mefy.platemate.presentation.app.providers

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.mefy.platemate.domain.model.language.AppLanguage
import java.util.Locale
import androidx.core.text.layoutDirection

@Composable
fun ProvideLocale(
    language: AppLanguage,
    content: @Composable () -> Unit
) {
    val locale = remember(language) {
        Locale.forLanguageTag(language.headerValue)
    }

    val baseContext = LocalContext.current
    val configuration = LocalConfiguration.current

    val localizedContext = remember(
        language,
        configuration.locales,
        configuration.fontScale,
        configuration.orientation
    ) {
        val config = Configuration(configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
        LocaleContextWrapper(baseContext, config)
    }

    val layoutDirection = remember(language) {
        when (locale.layoutDirection) {
            View.LAYOUT_DIRECTION_RTL -> LayoutDirection.Rtl
            else -> LayoutDirection.Ltr
        }
    }

    SideEffect {
        Locale.setDefault(locale)
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalLayoutDirection provides layoutDirection
    ) {
        content()
    }
}
private class LocaleContextWrapper(
    base: Context,
    overrideConfiguration: Configuration
) : ContextWrapper(base) {

    private val localizedResources: Resources =
        base.createConfigurationContext(overrideConfiguration).resources

    override fun getResources(): Resources = localizedResources
}
