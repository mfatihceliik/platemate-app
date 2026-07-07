package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.presentation.features.uimodel.AppearanceMode
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
@Composable
internal fun AppearanceSelector(
    themeMode: AppThemeMode,
    onModeSelected: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(colors.searchFieldBg)
            .padding(dims.spacing.s4),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4)
    ) {
        AppearanceMode.entries.forEach { mode ->
            AppearanceTab(
                label = stringResource(mode.labelRes),
                icon = mode.icon,
                isSelected = themeMode == mode.appThemeMode,
                onClick = { onModeSelected(mode.appThemeMode) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
