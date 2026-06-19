package com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AvatarEditSection(
    initials: String,
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
        Box(
            modifier = Modifier.size(dims.sizing.avatarHero),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(dims.sizing.avatarHero)
                    .clip(CircleShape)
                    .background(colors.primaryContainer)
                    .border(dims.stroke.st3, colors.primaryContainerBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = initials,
                    style = PMTextStyle.Headline,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.onPrimaryContainer
                )
            }
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .shadow(elevation = dims.spacing.s4, shape = CircleShape, spotColor = primary.copy(alpha = 0.45f))
                    .clip(CircleShape)
                    .background(primary)
                    .border(dims.stroke.st2, colors.surface, CircleShape)
                    .debouncedClickable(onClick = onAvatarClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.edit_profile_change_photo),
                    tint = Color.White,
                    modifier = Modifier.size(dims.sizing.avatarIconInner)
                )
            }
        }

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
        AvatarEditSection(initials = "AY", onAvatarClick = {})
    }
}

@Preview(name = "AvatarEditSection Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AvatarEditSectionDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AvatarEditSection(initials = "AY", onAvatarClick = {})
    }
}
