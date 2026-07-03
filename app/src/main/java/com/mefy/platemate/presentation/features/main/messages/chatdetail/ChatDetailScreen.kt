package com.mefy.platemate.presentation.features.main.messages.chatdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.components.model.PMButtonStyle
import com.mefy.platemate.presentation.features.main.messages.chatdetail.components.ChatDetailSettingsCard
import com.mefy.platemate.presentation.features.main.messages.chatdetail.components.ChatDetailUserCard
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ChatDetailScreen(
    state: ChatDetailUiState,
    onAction: (ChatDetailUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.chatdetail_title),
            onBackClick = { onAction(ChatDetailUiAction.BackClicked) }
        ),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            item(key = "user_card") {
                ChatDetailUserCard(
                    participantName = state.participantName,
                    initials = state.initials,
                    avatarBg = state.avatarBg,
                    avatarFg = state.avatarFg,
                    onMessageClick = { onAction(ChatDetailUiAction.MessageClicked) },
                    onProfileClick = { onAction(ChatDetailUiAction.ProfileClicked) },
                    onBlockClick = { onAction(ChatDetailUiAction.BlockClicked) }
                )
            }

            item(key = "settings_card") {
                ChatDetailSettingsCard(
                    notificationsEnabled = state.notificationsEnabled,
                    onNotificationsToggled = { onAction(ChatDetailUiAction.NotificationsToggled) },
                    onMediaClick = {},
                    onDeleteClick = { onAction(ChatDetailUiAction.DeleteChatClicked) },
                    onReportClick = { onAction(ChatDetailUiAction.ReportClicked) }
                )
            }

            item(key = "block_button") {
                PMButton(
                    text = stringResource(
                        if (state.isBlocked) R.string.chatdetail_unblock else R.string.chatdetail_block
                    ),
                    onClick = { onAction(ChatDetailUiAction.BlockClicked) },
                    style = PMButtonStyle.Outlined,
                    enabled = state.otherUserId != null && !state.isBlockInProgress,
                    loading = state.isBlockInProgress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
            onPrimaryClick = { onAction(ChatDetailUiAction.DeleteConfirmed) },
            primaryButtonColors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            secondaryText = stringResource(R.string.common_cancel),
            onSecondaryClick = { onAction(ChatDetailUiAction.DeleteDismissed) },
            onDismissRequest = { onAction(ChatDetailUiAction.DeleteDismissed) }
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
        ChatDetailScreen(state = previewState(), onAction = {})
    }
}

@Preview(name = "ChatDetail Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ChatDetailScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ChatDetailScreen(
            state = previewState().copy(notificationsEnabled = false),
            onAction = {}
        )
    }
}
