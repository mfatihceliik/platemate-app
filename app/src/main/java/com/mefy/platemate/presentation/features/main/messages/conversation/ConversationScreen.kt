package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMTopBarConfig
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ConversationTopBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.DateSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.MessageInputBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ReceivedBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.SentBubble
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onAction: (ConversationUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val listState = rememberLazyListState()

    LaunchedEffect(state.items.size) {
        if (state.items.isNotEmpty()) {
            listState.scrollToItem(state.items.lastIndex)
        }
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Custom {
            ConversationTopBar(
                participantName = state.participantName,
                initials = state.initials,
                avatarBg = state.avatarBg,
                avatarFg = state.avatarFg,
                onBackClick = { onAction(ConversationUiAction.BackClicked) },
                onInfoClick = { onAction(ConversationUiAction.InfoClicked) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            MessageInputBar(
                text = state.inputText,
                onTextChange = { onAction(ConversationUiAction.InputChanged(it)) },
                onSend = { onAction(ConversationUiAction.SendClicked) }
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = dims.spacing.s12,
                    vertical = dims.spacing.s10
                ),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
            ) {
                itemsIndexed(
                    items = state.items,
                    key = { index, item ->
                        when (item) {
                            is ConversationListItem.DateHeader -> "date_${item.label}"
                            is ConversationListItem.Message -> "msg_${item.model.id}_$index"
                        }
                    }
                ) { _, item ->
                    when (item) {
                        is ConversationListItem.DateHeader ->
                            DateSeparator(label = item.label)

                        is ConversationListItem.Message -> {
                            val msg = item.model
                            if (msg.isMine) {
                                SentBubble(
                                    content = msg.content,
                                    time = msg.time,
                                    isRead = msg.isRead
                                )
                            } else {
                                ReceivedBubble(
                                    initials = state.initials,
                                    avatarBg = state.avatarBg,
                                    avatarFg = state.avatarFg,
                                    content = msg.content,
                                    time = msg.time
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
