package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.presentation.theme.pmDimensions

private enum class AppearanceMode(
    val appThemeMode: AppThemeMode,
    @StringRes val labelRes: Int
) {
    LIGHT(AppThemeMode.LIGHT, R.string.profile_theme_light),
    DARK(AppThemeMode.DARK, R.string.profile_theme_dark),
    SYSTEM(AppThemeMode.SYSTEM, R.string.profile_theme_system)
}

private val SelectorTrackColor = Color(0xFFEAEFF4)

@Composable
internal fun AppearanceSelector(
    themeMode: AppThemeMode,
    onModeSelected: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(SelectorTrackColor)
            .padding(dims.spacing.s4)
    ) {
        AppearanceMode.entries.forEach { mode ->
            AppearanceTab(
                label = stringResource(mode.labelRes),
                isSelected = themeMode == mode.appThemeMode,
                onClick = { onModeSelected(mode.appThemeMode) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
