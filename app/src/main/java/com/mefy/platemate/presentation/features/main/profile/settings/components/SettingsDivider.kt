package com.mefy.platemate.presentation.features.main.profile.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SettingsDivider() {
    val dims = MaterialTheme.pmDimensions
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dims.spacing.s12)
            .height(dims.stroke.st1)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}
