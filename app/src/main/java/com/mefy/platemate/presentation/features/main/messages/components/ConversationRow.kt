package com.mefy.platemate.presentation.features.main.messages.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMMessageItem
import com.mefy.platemate.presentation.components.PMSwipeAction
import com.mefy.platemate.presentation.components.PMSwipeActionRow
import com.mefy.platemate.presentation.features.main.messages.MessageConversationUiModel
import com.mefy.platemate.presentation.theme.pmColors

@Composable
internal fun ConversationRow(
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
            name = conversation.name,
            preview = conversation.preview,
            time = conversation.time,
            unreadCount = conversation.unreadCount,
            onClick = onClick,
            isSentByMe = conversation.isSentByMe
        )
    }
}