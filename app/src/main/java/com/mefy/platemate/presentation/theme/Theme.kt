package com.mefy.platemate.presentation.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlateMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    accentColor: Color? = null,
    content: @Composable () -> Unit
) {
    // 1. Single source of truth: PMColors
    val basePMColors = if (darkTheme) DarkPMColors else LightPMColors
    val pmColors = if (accentColor != null) {
        basePMColors.withAccentColor(accentColor, darkTheme)
    } else {
        basePMColors
    }

    // 2. Derive Material3 ColorScheme for built-in components (Button, Card etc.)
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> pmColors.toColorScheme(darkTheme)
    }

    CompositionLocalProvider(
        LocalPMDimensions provides DefaultPMDimensions,
        LocalPMColors provides pmColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = PMShapes,
            content = content
        )
    }
}

@Preview(name = "PlateMate Theme Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateMateThemeLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ThemePreviewContent()
    }
}

@Preview(name = "PlateMate Theme Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateMateThemeDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ThemePreviewContent()
    }
}

@Composable
private fun ThemePreviewContent() {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Text(
            text = "PlateMate",
            color = colors.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(dims.spacing.s12)) {
                Text(
                    text = "Driver Reputation",
                    color = colors.onSurfaceVariant
                )
                Text(
                    text = "Primary emphasis sample",
                    color = colors.primary
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            Button(onClick = {}) {
                Text("Primary CTA")
            }
            OutlinedButton(onClick = {}) {
                Text("Secondary")
            }
        }
        Surface(
            color = colors.errorContainer
        ) {
            Text(
                text = "Error feedback preview",
                color = colors.onErrorContainer,
                modifier = Modifier.padding(dims.spacing.s12)
            )
        }
    }
}
