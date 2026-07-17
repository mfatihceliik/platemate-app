package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

import com.mefy.platemate.presentation.components.variant.PMButtonVariant

@Composable
internal fun UserProfileActionButtons(
    friendshipStatus: String,
    onAddFriendClick: () -> Unit,
    onCancelRequestClick: () -> Unit,
    onAcceptRequestClick: () -> Unit,
    onRemoveFriendClick: () -> Unit,
    onMessageClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val buttonModifier = Modifier.weight(1f)
        when (friendshipStatus) {
            "NONE" -> PMButton(text = stringResource(R.string.user_profile_add_friend), onClick = onAddFriendClick, modifier = buttonModifier)
            "PENDING_SENT" -> PMButton(text = stringResource(R.string.user_profile_cancel_request), onClick = onCancelRequestClick, variant = PMButtonVariant.Outlined, modifier = buttonModifier)
            "PENDING_RECEIVED" -> PMButton(text = stringResource(R.string.user_profile_accept_request), onClick = onAcceptRequestClick, modifier = buttonModifier)
            "FRIENDS" -> PMButton(text = stringResource(R.string.user_profile_friends), onClick = onRemoveFriendClick, variant = PMButtonVariant.Outlined, modifier = buttonModifier)
            else -> PMButton(text = stringResource(R.string.user_profile_add_friend), onClick = onAddFriendClick, modifier = buttonModifier)
        }
        
        PMButton(
            text = stringResource(R.string.user_profile_message),
            onClick = onMessageClick,
            modifier = Modifier.weight(1f)
        )
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
            UserProfileActionButtons("NONE", {}, {}, {}, {}, {}, {})
            UserProfileActionButtons("PENDING_SENT", {}, {}, {}, {}, {}, {})
            UserProfileActionButtons("FRIENDS", {}, {}, {}, {}, {}, {})
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
            UserProfileActionButtons("NONE", {}, {}, {}, {}, {}, {})
            UserProfileActionButtons("PENDING_SENT", {}, {}, {}, {}, {}, {})
            UserProfileActionButtons("FRIENDS", {}, {}, {}, {}, {}, {})
        }
    }
}
