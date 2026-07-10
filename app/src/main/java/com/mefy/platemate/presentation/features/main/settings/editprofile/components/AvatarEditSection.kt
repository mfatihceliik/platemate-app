package com.mefy.platemate.presentation.features.main.settings.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AvatarEditSection(
    displayName: String,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val primary = colors.primary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        PMAvatar(
            displayName = displayName,
            isEditable = true,
            onEditClick = onAvatarClick
        )

        PMText(
            text = stringResource(R.string.edit_profile_change_photo),
            style = PMTextStyle.Body,
            fontWeight = FontWeight.SemiBold,
            color = primary,
            modifier = Modifier.debouncedClickable(onClick = onAvatarClick)
        )
    }

    HorizontalDivider(color = colors.outlineVariant)
}

@Preview(name = "AvatarEditSection Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun AvatarEditSectionLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AvatarEditSection(displayName = "Ahmet Yılmaz", onAvatarClick = {})
    }
}

@Preview(name = "AvatarEditSection Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AvatarEditSectionDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AvatarEditSection(displayName = "Ahmet Yılmaz", onAvatarClick = {})
    }
}
