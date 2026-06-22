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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMLoading
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ConversationTopBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.DateSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.MessageInputBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ReceivedBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.SentBubble
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
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
        containerColor = MaterialTheme.pmColors.surface,
        status = when {
            state.isLoading -> ScreenStatus.Loading
            state.errorMessage != null -> ScreenStatus.Error(state.errorMessage)
            else -> ScreenStatus.Content
        },
        onRetry = { onAction(ConversationUiAction.RetryClicked) },
        loading = { innerPadding -> PMLoading(modifier = Modifier.padding(innerPadding)) },
        bottomBar = {
            if (state.errorMessage == null) {
                MessageInputBar(
                    text = state.inputText,
                    onTextChange = { onAction(ConversationUiAction.InputChanged(it)) },
                    onSend = { onAction(ConversationUiAction.SendClicked) }
                )
            }
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
                    vertical = dims.spacing.s8
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

private val previewState = ConversationUiState(
    isLoading = false,
    participantName = "Ahmet Yılmaz",
    initials = "AY",
    avatarBg = Color(0xFFECFEFF),
    avatarFg = Color(0xFF0E7490),
    inputText = "",
    items = listOf(
        ConversationListItem.DateHeader(label = "Bugün"),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 1L,
                content = "Merhaba, plakamı gördünüz mü?",
                time = "10:42",
                isMine = false,
                isRead = true
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 2L,
                content = "Evet, 34 EK 0682 değil mi?",
                time = "10:43",
                isMine = true,
                isRead = true
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 3L,
                content = "Evet aynen o! Teşekkürler.",
                time = "10:44",
                isMine = false,
                isRead = true
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 4L,
                content = "Rica ederim, iyi günler!",
                time = "10:45",
                isMine = true,
                isRead = false
            )
        ),
    )
)

@Preview(name = "ConversationScreen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ConversationScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ConversationScreen(state = previewState, onAction = {})
    }
}

@Preview(name = "ConversationScreen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ConversationScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ConversationScreen(state = previewState, onAction = {})
    }
}
