package com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun DangerZoneSection(
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val shape = MaterialTheme.shapes.small
    val errorColor = MaterialTheme.colorScheme.error

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.errorContainer)
            .border(dims.stroke.st1, errorColor.copy(alpha = 0.3f), shape)
            .debouncedClickable(onClick = onDeleteAccountClick)
            .padding(dims.spacing.s12),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = errorColor,
            modifier = Modifier.size(dims.spacing.s16)
        )
        PMText(
            text = stringResource(R.string.edit_profile_delete_account),
            style = PMTextStyle.Body,
            fontWeight = FontWeight.SemiBold,
            color = errorColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = errorColor,
            modifier = Modifier.size(dims.spacing.s16)
        )
    }
}

@Preview(name = "DangerZoneSection Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DangerZoneSectionLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DangerZoneSection(onDeleteAccountClick = {})
    }
}

@Preview(name = "DangerZoneSection Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun DangerZoneSectionDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        DangerZoneSection(onDeleteAccountClick = {})
    }
}
