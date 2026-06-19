package com.mefy.platemate.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun PMSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = MaterialTheme.pmColors
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedTrackColor = colors.primary,
            uncheckedTrackColor = colors.outline,
            checkedThumbColor = Color.White,
            uncheckedThumbColor = Color.White,
            uncheckedBorderColor = Color.Transparent,
            checkedBorderColor = Color.Transparent
        )
    )
}