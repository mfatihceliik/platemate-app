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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

private val DarkColorScheme = darkColorScheme(
    primary = PlateDarkPrimary,
    onPrimary = PlateDarkOnPrimary,
    primaryContainer = PlateDarkPrimaryContainer,
    onPrimaryContainer = PlateDarkOnPrimaryContainer,
    secondary = PlateDarkSecondary,
    onSecondary = PlateDarkOnSecondary,
    secondaryContainer = PlateDarkSecondaryContainer,
    onSecondaryContainer = PlateDarkOnSecondaryContainer,
    tertiary = PlateDarkTertiary,
    onTertiary = PlateDarkOnTertiary,
    tertiaryContainer = PlateDarkTertiaryContainer,
    onTertiaryContainer = PlateDarkOnTertiaryContainer,
    background = PlateDarkBackground,
    onBackground = PlateDarkOnBackground,
    surface = PlateDarkSurface,
    onSurface = PlateDarkOnSurface,
    surfaceVariant = PlateDarkSurfaceVariant,
    onSurfaceVariant = PlateDarkOnSurfaceVariant,
    surfaceTint = PlateDarkPrimary,
    inverseSurface = PlateDarkInverseSurface,
    inverseOnSurface = PlateDarkInverseOnSurface,
    error = PlateDarkError,
    onError = PlateDarkOnError,
    errorContainer = PlateDarkErrorContainer,
    onErrorContainer = PlateDarkOnErrorContainer,
    outline = PlateDarkOutline,
    outlineVariant = PlateDarkOutlineVariant,
    scrim = PlateDarkScrim
)

private val LightColorScheme = lightColorScheme(
    primary = PlateLightPrimary,
    onPrimary = PlateLightOnPrimary,
    primaryContainer = PlateLightPrimaryContainer,
    onPrimaryContainer = PlateLightOnPrimaryContainer,
    secondary = PlateLightSecondary,
    onSecondary = PlateLightOnSecondary,
    secondaryContainer = PlateLightSecondaryContainer,
    onSecondaryContainer = PlateLightOnSecondaryContainer,
    tertiary = PlateLightTertiary,
    onTertiary = PlateLightOnTertiary,
    tertiaryContainer = PlateLightTertiaryContainer,
    onTertiaryContainer = PlateLightOnTertiaryContainer,
    background = PlateLightBackground,
    onBackground = PlateLightOnBackground,
    surface = PlateLightSurface,
    onSurface = PlateLightOnSurface,
    surfaceVariant = PlateLightSurfaceVariant,
    onSurfaceVariant = PlateLightOnSurfaceVariant,
    surfaceTint = PlateLightPrimary,
    inverseSurface = PlateLightInverseSurface,
    inverseOnSurface = PlateLightInverseOnSurface,
    error = PlateLightError,
    onError = PlateLightOnError,
    errorContainer = PlateLightErrorContainer,
    onErrorContainer = PlateLightOnErrorContainer,
    outline = PlateLightOutline,
    outlineVariant = PlateLightOutlineVariant,
    scrim = PlateLightScrim
)

@Composable
fun PlateMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    accentColor: Color? = null,
    content: @Composable () -> Unit
) {
    val pmColors = if (darkTheme) PMColorsDark else PMColorsLight
    val baseScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val colorScheme = if (accentColor != null) {
        baseScheme.withAccentColor(accentColor, darkTheme)
    } else {
        baseScheme
    }

    CompositionLocalProvider(
        LocalPMDimensions provides DefaultPMDimensions,
        LocalPMColors provides pmColors,
        LocalPMIconColors provides pmColors.icon
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = PMShapes,
            content = content
        )
    }
}

private fun ColorScheme.withAccentColor(accent: Color, isDark: Boolean): ColorScheme {
    val onAccent = if (accent.luminance() > 0.179f) Color(0xFF1E293B) else Color.White
    val container = if (isDark) {
        lerpColor(accent, Color(0xFF0F172A), 0.65f)
    } else {
        lerpColor(accent, Color.White, 0.82f)
    }
    val onContainer = if (isDark) {
        lerpColor(accent, Color.White, 0.65f)
    } else {
        lerpColor(accent, Color(0xFF0F172A), 0.45f)
    }
    return copy(
        primary = accent,
        onPrimary = onAccent,
        primaryContainer = container,
        onPrimaryContainer = onContainer,
        surfaceTint = accent
    )
}

private fun lerpColor(a: Color, b: Color, t: Float) = Color(
    red = a.red + (b.red - a.red) * t,
    green = a.green + (b.green - a.green) * t,
    blue = a.blue + (b.blue - a.blue) * t,
    alpha = 1f
)

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.pmDimensions.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s12)
    ) {
        Text(
            text = "PlateMate",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(MaterialTheme.pmDimensions.spacing.s12)) {
                Text(
                    text = "Driver Reputation",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Primary emphasis sample",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s8)) {
            Button(onClick = {}) {
                Text("Primary CTA")
            }
            OutlinedButton(onClick = {}) {
                Text("Secondary")
            }
        }
        Surface(
            color = MaterialTheme.colorScheme.errorContainer
        ) {
            Text(
                text = "Error feedback preview",
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(MaterialTheme.pmDimensions.spacing.s12)
            )
        }
    }
}
