package com.mefy.platemate.presentation.features.main.messages.chatdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.features.main.messages.chatdetail.components.ChatDetailUserCard
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ChatDetailScreen(
    modifier: Modifier = Modifier,
    state: ChatDetailUiState,
    onAction: (ChatDetailUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val onMessageClicked = remember(onAction) { { onAction(ChatDetailUiAction.MessageClicked) } }
    val onProfileClicked = remember(onAction) { { onAction(ChatDetailUiAction.ProfileClicked) } }
    val onBlockClicked = remember(onAction) { { onAction(ChatDetailUiAction.BlockClicked) } }
    val onDeleteConfirmed = remember(onAction) { { onAction(ChatDetailUiAction.DeleteConfirmed) } }
    val onDeleteDismissed = remember(onAction) { { onAction(ChatDetailUiAction.DeleteDismissed) } }

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentPadding = PaddingValues(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        item(key = "user_card") {
            ChatDetailUserCard(
                participantName = state.participantName,
                initials = state.initials,
                avatarBg = state.avatarBg,
                avatarFg = state.avatarFg,
                onMessageClick = onMessageClicked,
                onProfileClick = onProfileClicked,
                onBlockClick = onBlockClicked
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.chatdetail_notifications),
                leadingIcon = Icons.Outlined.Notifications,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                trailing = {
                    PMSwitch(
                        checked = true,
                        onCheckedChange = {}
                    )
                }
            )
            PMRowItem(
                title = stringResource(R.string.chatdetail_media_files),
                leadingIcon = Icons.Outlined.PhotoLibrary,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
            )
            PMRowItem(
                title = stringResource(R.string.chatdetail_delete_chat),
                leadingIcon = Icons.Outlined.DeleteOutline,
                leadingIconTint = colors.error,
                leadingContainerColor = colors.errorContainer,
            )
            PMRowItem(
                title = stringResource(R.string.chatdetail_report),
                leadingIcon = Icons.Outlined.Flag,
                leadingIconTint = colors.error,
                leadingContainerColor = colors.errorContainer,
            )
        }
    }

    if (state.showDeleteConfirmation) {
        PMPopup(
            title = stringResource(R.string.messages_delete_confirm_title),
            message = stringResource(R.string.messages_delete_confirm_message),
            icon = Icons.Filled.Delete,
            iconTint = colors.error,
            iconContainerColor = colors.errorContainer,
            primaryText = stringResource(R.string.common_delete),
            onPrimaryClick = onDeleteConfirmed,
            primaryButtonColors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            secondaryText = stringResource(R.string.common_cancel),
            onSecondaryClick = onDeleteDismissed,
            onDismissRequest = onDeleteDismissed
        )
    }
}

private fun previewState() = ChatDetailUiState(
    conversationId = "1",
    participantName = "Ahmet Yılmaz",
    initials = "AY",
    avatarBg = Color(0xFFEEF2FF),
    avatarFg = Color(0xFF4F46E5),
    notificationsEnabled = true
)

@Preview(name = "ChatDetail Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ChatDetailScreenPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ChatDetailScreen(state = previewState(), onAction = {},)
    }
}

@Preview(name = "ChatDetail Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ChatDetailScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ChatDetailScreen(
            state = previewState().copy(notificationsEnabled = false),
            onAction = {},
        )
    }
}
