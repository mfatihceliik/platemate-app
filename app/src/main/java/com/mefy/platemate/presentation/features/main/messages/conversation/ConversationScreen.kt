package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.features.main.messages.conversation.components.DateSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ReceivedBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ScrollToBottomButton
import com.mefy.platemate.presentation.features.main.messages.conversation.components.SentBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.UnreadMessagesSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.rememberRelativeDateLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    state: ConversationUiState,
    onAction: (ConversationUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
    // Route'taki composer overlay'inin ölçülen yüksekliği: son mesaj barın arkasında
    // kalmasın diye liste alt boşluğu ve "en alta in" butonu bu kadar yukarı itilir.
    bottomOverlayInset: Dp = 0.dp
) {
    val dims = MaterialTheme.pmDimensions
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollDown by remember { derivedStateOf { listState.canScrollForward } }
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val items = state.items

    var initialScrollDone by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(items.size, state.firstUnreadIndex) {
        if (items.isEmpty()) return@LaunchedEffect
        if (!initialScrollDone) {
            listState.scrollToItem(state.firstUnreadIndex ?: items.lastIndex)
            initialScrollDone = true
        } else {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val nearBottom = lastVisible >= items.lastIndex - 3
            val lastIsMine =
                (items.lastOrNull() as? ConversationListItem.Message)?.model?.isMine == true
            if (nearBottom || lastIsMine) {
                listState.animateScrollToItem(items.lastIndex)
            }
        }
    }

    LaunchedEffect(listState) {
        var wasAtBottom = !listState.canScrollForward
        var prevImeBottom = 0
        snapshotFlow { imeInsets.getBottom(density) to listState.canScrollForward }
            .collect { (imeBottomPx, canScrollForward) ->
                if (imeBottomPx == 0) {
                    wasAtBottom = !canScrollForward
                } else if (imeBottomPx > prevImeBottom && wasAtBottom) {
                    val last = listState.layoutInfo.totalItemsCount - 1
                    if (last >= 0) listState.scrollToItem(last)
                }
                prevImeBottom = imeBottomPx
            }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = innerPadding.calculateLeftPadding(androidx.compose.ui.platform.LocalLayoutDirection.current),
                end = innerPadding.calculateRightPadding(androidx.compose.ui.platform.LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + bottomOverlayInset
            ),
            verticalArrangement = spacedByWithFooter(dims.spacing.s16) //Arrangement.spacedBy(dims.spacing.s12)
        ) {
            items(
                items = items,
                key = { item ->
                    when (item) {
                        is ConversationListItem.DateHeader -> "date_${item.isoDate}"
                        is ConversationListItem.Message -> "msg_${item.model.id}"
                        is ConversationListItem.UnreadDivider -> "unread_divider"
                    }
                },
                contentType = { item ->
                    when (item) {
                        is ConversationListItem.DateHeader -> "date"
                        is ConversationListItem.Message -> "message"
                        is ConversationListItem.UnreadDivider -> "unread_divider"
                    }
                }
            ) { item ->
                when (item) {
                    is ConversationListItem.DateHeader ->
                        DateSeparator(label = rememberRelativeDateLabel(item.isoDate))

                    is ConversationListItem.UnreadDivider ->
                        UnreadMessagesSeparator(count = item.count)

                    is ConversationListItem.Message -> {
                        val msg = item.model
                        if (msg.isMine) {
                            SentBubble(
                                content = msg.content,
                                time = msg.time,
                                status = msg.status
                            )
                        } else {
                            ReceivedBubble(
                                senderName = state.participantName,
                                content = msg.content,
                                time = msg.time
                            )
                        }
                    }
                }
            }
        }

        ScrollToBottomButton(
            visible = showScrollDown,
            onClick = { scope.launch { listState.animateScrollToItem(items.lastIndex) } },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = dims.spacing.s16, bottom = bottomOverlayInset + dims.spacing.s16)
        )
    }
}

private val previewState = ConversationUiState(
    isLoading = false,
    participantName = "Ahmet Yılmaz",
    inputText = "",
    items = listOf(
        ConversationListItem.DateHeader(isoDate = "2026-07-04"),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 1L,
                content = "Merhaba, plakamı gördünüz mü",
                time = "10:42",
                isMine = false,
                status = MessageStatus.READ
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 2L,
                content = "Evet, 34 EK 0682 değil mi?",
                time = "10:43",
                isMine = true,
                status = MessageStatus.READ
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 3L,
                content = "Evet aynen o! Teşekkürler.",
                time = "10:44",
                isMine = false,
                status = MessageStatus.READ
            )
        ),
        ConversationListItem.Message(
            ChatMessageUiModel(
                id = 4L,
                content = "Rica ederim, iyi günler!",
                time = "10:45",
                isMine = true,
                status = MessageStatus.SENT
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
