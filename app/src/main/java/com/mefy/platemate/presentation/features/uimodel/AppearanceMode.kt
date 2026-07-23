package com.mefy.platemate.presentation.features.uimodel

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.theme.AppThemeMode

enum class AppearanceMode(
    val appThemeMode: AppThemeMode,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector
) {
    LIGHT(AppThemeMode.LIGHT, R.string.profile_theme_light, Icons.Outlined.LightMode),
    DARK(AppThemeMode.DARK, R.string.profile_theme_dark, Icons.Outlined.DarkMode),
    SYSTEM(AppThemeMode.SYSTEM, R.string.profile_theme_system, Icons.Outlined.BrightnessAuto)
}