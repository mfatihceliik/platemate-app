package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.profile.ProfileFriendshipStatus
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.features.main.profile.userprofile.mapper.FriendshipActionButtonMapper
import com.mefy.platemate.presentation.features.main.profile.userprofile.mapper.FriendshipActionButtonMappingInput
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun UserProfileActionButtons(
    modifier: Modifier = Modifier,
    friendshipStatus: ProfileFriendshipStatus,
    onAddFriendClick: () -> Unit,
    onCancelRequestClick: () -> Unit,
    onAcceptRequestClick: () -> Unit,
    onRemoveFriendClick: () -> Unit,
    onMessageClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    PMCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val buttonModifier = Modifier.weight(1f)
            val primaryAction = FriendshipActionButtonMapper().map(
                FriendshipActionButtonMappingInput(
                    friendshipStatus = friendshipStatus,
                    onAddFriendClick = onAddFriendClick,
                    onCancelRequestClick = onCancelRequestClick,
                    onAcceptRequestClick = onAcceptRequestClick,
                    onRemoveFriendClick = onRemoveFriendClick
                )
            )
            PMButton(
                text = stringResource(primaryAction.labelRes),
                onClick = primaryAction.onClick,
                variant = primaryAction.variant,
                buttonColors = if (primaryAction.isDestructive) {
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.error,
                    )
                } else {
                    null
                },
                modifier = buttonModifier
            )

            PMButton(
                text = stringResource(R.string.user_profile_message),
                onClick = onMessageClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(name = "UserProfileActionButtons Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileActionButtonsLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val spacing = PMTheme.spacing
        Column(
            modifier = Modifier.padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            UserProfileActionButtons(
                Modifier,
                ProfileFriendshipStatus.NONE,
                {},
                {},
                {},
                {},
                {},
                {}
            )
            UserProfileActionButtons(
                Modifier,
                ProfileFriendshipStatus.PENDING_SENT,
                {},
                {},
                {},
                {},
                {},
                {}
            )
            UserProfileActionButtons(
                Modifier,
                ProfileFriendshipStatus.FRIENDS,
                {},
                {},
                {},
                {},
                {},
                {}
            )
        }
    }
}

@Preview(name = "UserProfileActionButtons Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileActionButtonsDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val spacing = PMTheme.spacing
        Column(
            modifier = Modifier.padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            UserProfileActionButtons(Modifier,
                ProfileFriendshipStatus.NONE,
                {},
                {},
                {},
                {},
                {},
                {}
            )
            UserProfileActionButtons(Modifier,
                ProfileFriendshipStatus.PENDING_SENT,
                {},
                {},
                {},
                {},
                {},
                {}
            )
            UserProfileActionButtons(Modifier,
                ProfileFriendshipStatus.FRIENDS,
                {},
                {},
                {},
                {},
                {},
                {}
            )
        }
    }
}
