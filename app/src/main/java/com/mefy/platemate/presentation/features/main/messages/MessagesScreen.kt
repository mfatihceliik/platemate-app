package com.mefy.platemate.presentation.features.main.messages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMMessageItem
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.components.PMSwipeAction
import com.mefy.platemate.presentation.components.PMSwipeActionRow
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun MessagesScreen(
    modifier: Modifier = Modifier,
    state: MessagesUiState,
    onAction: (MessagesUiAction) -> Unit,
    innerPadding: PaddingValues
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
    ) {
        items(
            items = state.conversations, key = { it.roomId }) { conversation ->
            ConversationRow(
                conversation = conversation,
                onClick = { onAction(MessagesUiAction.ConversationClicked(conversation.roomId)) },
                onSwipeToDelete = { onAction(MessagesUiAction.DeleteSwiped(conversation.roomId)) },
                onSwipeToMarkRead = { onAction(MessagesUiAction.MarkReadSwiped(conversation.roomId)) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = dims.spacing.s16 + dims.sizing.avatarMedium + dims.spacing.s12),
                color = colors.outlineVariant
            )
        }
    }

    if (state.pendingDeleteRoomId != null) {
        PMPopup(
            title = stringResource(R.string.messages_delete_confirm_title),
            message = stringResource(R.string.messages_delete_confirm_message),
            icon = Icons.Filled.Delete,
            iconTint = colors.error,
            iconContainerColor = colors.errorContainer,
            primaryText = stringResource(R.string.common_delete),
            onPrimaryClick = { onAction(MessagesUiAction.DeleteConfirmed) },
            primaryButtonColors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            secondaryText = stringResource(R.string.common_cancel),
            onSecondaryClick = { onAction(MessagesUiAction.DeleteDismissed) },
            onDismissRequest = { onAction(MessagesUiAction.DeleteDismissed) }
        )
    }
}

@Composable
private fun ConversationRow(
    conversation: MessageConversationUiModel,
    onClick: () -> Unit,
    onSwipeToDelete: () -> Unit,
    onSwipeToMarkRead: () -> Unit,
) {
    val colors = MaterialTheme.pmColors

    // Swipe right → mark read is only offered when there's something to clear; an already-read row
    // simply has no leading action (and no right-swipe).
    val markReadAction = if (conversation.unreadCount > 0) {
        PMSwipeAction(
            icon = Icons.Filled.DoneAll,
            label = stringResource(R.string.messages_mark_read),
            containerColor = colors.success,
            contentColor = Color.White
        )
    } else null

    PMSwipeActionRow(
        startToEndAction = markReadAction,
        endToStartAction = PMSwipeAction(
            icon = Icons.Filled.Delete,
            label = stringResource(R.string.common_delete),
            containerColor = colors.error,
            contentColor = colors.onError
        ),
        onStartToEnd = onSwipeToMarkRead,
        onEndToStart = onSwipeToDelete
    ) {
        PMMessageItem(
            initials = conversation.initials,
            name = conversation.name,
            preview = conversation.preview,
            time = conversation.time,
            unreadCount = conversation.unreadCount,
            avatarBg = conversation.avatarBg,
            avatarFg = conversation.avatarFg,
            onClick = onClick
        )
    }
}

@Preview(name = "Messages Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false, conversations = previewConversations()
            ), onAction = {}, innerPadding = PaddingValues()
        )
    }
}


@Preview(name = "Messages Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun MessagesDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false, conversations = previewConversations()
            ), onAction = {}, innerPadding = PaddingValues()
        )
    }
}

private fun previewConversations() = listOf(
    MessageConversationUiModel(
        roomId = 1,
        initials = "AY",
        name = "Ahmet Y.",
        preview = "Teşekkürler, çok yardımcı oldun!",
        time = "09:24",
        unreadCount = 3,
        avatarBg = Color(0xFFEEF2FF),
        avatarFg = Color(0xFF4F46E5)
    ), MessageConversationUiModel(
        roomId = 2,
        initials = "ZK",
        name = "Zeynep K.",
        preview = "Plakayı gördüm, gerçekten nazik biri",
        time = "Dün",
        unreadCount = 0,
        avatarBg = Color(0xFFECFEFF),
        avatarFg = Color(0xFF0891B2)
    ), MessageConversationUiModel(
        roomId = 3,
        initials = "MC",
        name = "Mehmet C.",
        preview = "Evet, o plakanın sahibiyim ben",
        time = "Pzt",
        unreadCount = 0,
        avatarBg = Color(0xFFF0FDF4),
        avatarFg = Color(0xFF15803D)
    )
)
