package com.mefy.platemate.presentation.features.main.settings.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun LogoutIcon() {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.Logout,
        contentDescription = null,
        tint = MaterialTheme.pmColors.error,
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconMd)
    )
}
