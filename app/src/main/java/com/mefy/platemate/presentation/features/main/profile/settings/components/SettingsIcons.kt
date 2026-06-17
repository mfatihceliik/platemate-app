package com.mefy.platemate.presentation.features.main.profile.settings.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun LockIcon() {
    Icon(
        imageVector = Icons.Filled.Lock,
        contentDescription = null,
        tint = MaterialTheme.pmColors.primaryDark,
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconSm)
    )
}

@Composable
internal fun CrownIcon() {
    Icon(
        imageVector = Icons.Filled.Star,
        contentDescription = null,
        tint = MaterialTheme.pmColors.star,
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconMd)
    )
}

@Composable
internal fun DropletIcon() {
    Icon(
        imageVector = Icons.Filled.Palette,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconSm)
    )
}

@Composable
internal fun GlobeIcon() {
    Icon(
        imageVector = Icons.Filled.Language,
        contentDescription = null,
        tint = Color(0xFF15803D),
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconMd)
    )
}

@Composable
internal fun BellIcon() {
    Icon(
        imageVector = Icons.Filled.Notifications,
        contentDescription = null,
        tint = Color(0xFFE11D48),
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconMd)
    )
}

@Composable
internal fun LinkIcon() {
    Icon(
        imageVector = Icons.Filled.Link,
        contentDescription = null,
        tint = Color(0xFF7C3AED),
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconMd)
    )
}

@Composable
internal fun PersonIcon() {
    Icon(
        imageVector = Icons.Filled.Person,
        contentDescription = null,
        tint = MaterialTheme.pmColors.primaryDark,
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconMd)
    )
}

@Composable
internal fun LogoutIcon() {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.Logout,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error,
        modifier = Modifier.size(MaterialTheme.pmDimensions.sizing.iconMd)
    )
}
