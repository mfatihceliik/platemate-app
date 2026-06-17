package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.IosShare
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
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun UserProfileActionButtons(
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onMessageClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val buttonShape = MaterialTheme.shapes.small

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(dims.spacing.s48)
                .then(
                    if (isFollowing) Modifier.background(MaterialTheme.colorScheme.outline, buttonShape)
                    else Modifier
                        .shadow(elevation = dims.spacing.s4, shape = buttonShape, ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                        .background(MaterialTheme.colorScheme.primary, buttonShape)
                )
                .debouncedClickable(onClick = onFollowClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(
                text = if (isFollowing) stringResource(R.string.user_profile_following_label) else stringResource(R.string.user_profile_follow),
                style = PMTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = if (isFollowing) colors.textSecondary else Color.White
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(dims.spacing.s48)
                .clip(buttonShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(dims.stroke.st1, MaterialTheme.colorScheme.outline, buttonShape)
                .debouncedClickable(onClick = onMessageClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(
                text = stringResource(R.string.user_profile_message),
                style = PMTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
        }

        Row(
            modifier = Modifier
                .size(dims.spacing.s48)
                .clip(buttonShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(dims.stroke.st1, MaterialTheme.colorScheme.outline, buttonShape)
                .debouncedClickable(onClick = onShareClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.IosShare,
                contentDescription = stringResource(R.string.user_profile_share),
                tint = colors.textTertiary,
                modifier = Modifier.size(dims.sizing.iconMd)
            )
        }
    }
}

@Preview(name = "UserProfileActionButtons Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileActionButtonsLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier.padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            UserProfileActionButtons(isFollowing = false, onFollowClick = {}, onMessageClick = {}, onShareClick = {})
            UserProfileActionButtons(isFollowing = true, onFollowClick = {}, onMessageClick = {}, onShareClick = {})
        }
    }
}

@Preview(name = "UserProfileActionButtons Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileActionButtonsDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier.padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            UserProfileActionButtons(isFollowing = false, onFollowClick = {}, onMessageClick = {}, onShareClick = {})
            UserProfileActionButtons(isFollowing = true, onFollowClick = {}, onMessageClick = {}, onShareClick = {})
        }
    }
}
