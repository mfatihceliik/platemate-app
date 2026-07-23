package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMBottomSheet
import com.mefy.platemate.presentation.components.PMBottomSheetActions
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.messages.conversation.components.DateSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.DeletedBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ReceivedBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ReportMessageBottomSheet
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ScrollToBottomButton
import com.mefy.platemate.presentation.features.main.messages.conversation.components.SentBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.SwipeToReplyBox
import com.mefy.platemate.presentation.features.main.messages.conversation.components.SystemInfoBubble
import com.mefy.platemate.presentation.features.main.messages.conversation.components.UnreadMessagesSeparator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.rememberRelativeDateLabel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    state: ConversationUiState,
    onAction: (ConversationUiAction) -> Unit,
    onMessageLongPressed: (message: ChatMessageUiModel, bounds: Rect) -> Unit = { _, _ -> },
    innerPadding: PaddingValues = PaddingValues(),
    bottomOverlayInset: Dp = 0.dp
) {
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val radius = PMTheme.radius
    val shape = PMTheme.shapes

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollDown by remember { derivedStateOf { listState.canScrollForward } }
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val items = state.items

    val bubbleCoordinates = remember { mutableMapOf<Long, androidx.compose.ui.layout.LayoutCoordinates>() }

    var initialScrollDone by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(items.size, state.firstUnreadIndex, state.scrollToMessageIndex) {
        if (items.isEmpty()) return@LaunchedEffect
        if (!initialScrollDone) {
            listState.scrollToItem(state.scrollToMessageIndex ?: state.firstUnreadIndex ?: items.lastIndex)
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
                start = innerPadding.calculateLeftPadding(LocalLayoutDirection.current),
                end = innerPadding.calculateRightPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + bottomOverlayInset
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.s16)
        ) {
            items(
                items = items,
                key = { item ->
                    when (item) {
                        is ConversationListItem.DateHeader -> "date_${item.isoDate}"
                        is ConversationListItem.Message -> "msg_${item.model.id}"
                        is ConversationListItem.UnreadDivider -> "unread_divider"
                        is ConversationListItem.SystemInfo -> "system_info_${item.id}"
                    }
                },
                contentType = { item ->
                    when (item) {
                        is ConversationListItem.DateHeader -> "date"
                        is ConversationListItem.Message -> "message"
                        is ConversationListItem.UnreadDivider -> "unread_divider"
                        is ConversationListItem.SystemInfo -> "system_info"
                    }
                }
            ) { item ->
                when (item) {
                    is ConversationListItem.DateHeader ->
                        DateSeparator(label = rememberRelativeDateLabel(item.isoDate))

                    is ConversationListItem.UnreadDivider ->
                        UnreadMessagesSeparator(count = item.count)

                    is ConversationListItem.SystemInfo ->
                        SystemInfoBubble(
                            text = item.text.resolve(),
                            modifier = Modifier.padding(horizontal = spacing.s4)
                        )

                    is ConversationListItem.Message -> {
                        val msg = item.model
                        val longPressModifier = Modifier
                            .onGloballyPositioned { coordinates -> bubbleCoordinates[msg.id] = coordinates }
                            .combinedClickable(
                                onClick = {},
                                onLongClick = {
                                    bubbleCoordinates[msg.id]?.let { coordinates ->
                                        onMessageLongPressed(msg, coordinates.boundsInRoot())
                                    }
                                }
                            )
                        if (msg.status == MessageStatus.DELETED) {
                            DeletedBubble(
                                isMine = msg.isMine,
                                time = msg.time,
                                modifier = Modifier.padding(horizontal = spacing.s4)
                            )
                        } else {
                            SwipeToReplyBox(
                                onReply = { onAction(ConversationUiAction.QuoteMessageClicked(msg.id)) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (msg.isMine) {
                                    SentBubble(
                                        content = msg.content,
                                        time = msg.time,
                                        status = msg.status,
                                        modifier = Modifier
                                            .padding(horizontal = spacing.s4)
                                            .then(longPressModifier),
                                        onRetryClicked = { onAction(ConversationUiAction.RetryMessageClicked(msg.id)) },
                                        quotedPreview = msg.replyPreview
                                    )
                                } else {
                                    ReceivedBubble(
                                        senderName = state.participantName,
                                        content = msg.content,
                                        quotedPreview = msg.replyPreview,
                                        time = msg.time,
                                        modifier = Modifier
                                            .padding(horizontal = spacing.s4)
                                            .then(longPressModifier)
                                    )
                                }
                            }
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
                .padding(end = spacing.s16, bottom = bottomOverlayInset + spacing.s16)
        )
    }

    val deleteConfirmTargetId = state.deleteConfirmTargetId
    if (deleteConfirmTargetId != null) {
        PMBottomSheet(
            onDismiss = { onAction(ConversationUiAction.DeleteConfirmDismissed) },
            title = stringResource(R.string.conversation_delete_message_title)
        ) {
            PMText(
                text = stringResource(R.string.conversation_delete_message_body),
                style = PMTextStyle.Body,
                color = colors.textSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.s24, vertical = spacing.s16)
            )
            PMBottomSheetActions(
                cancelText = stringResource(R.string.common_cancel),
                submitText = stringResource(R.string.common_delete),
                onCancel = { onAction(ConversationUiAction.DeleteConfirmDismissed) },
                onSubmit = {
                    onAction(ConversationUiAction.DeleteMessageConfirmed(deleteConfirmTargetId))
                },
                modifier = Modifier
                    .padding(horizontal = spacing.s24)
                    .padding(bottom = spacing.s24)
            )
        }
    }

    val reportTarget = state.reportTarget
    if (reportTarget != null) {
        ReportMessageBottomSheet(
            reportTarget = reportTarget,
            selectedReason = state.selectedReportReason,
            onReasonSelected = { onAction(ConversationUiAction.ReportReasonSelected(it)) },
            otherReasonText = state.otherReportReasonText,
            onOtherReasonTextChanged = { onAction(ConversationUiAction.ReportOtherReasonTextChanged(it)) },
            isSubmitting = state.isSubmittingReport,
            onDismiss = { onAction(ConversationUiAction.ReportDismissed) },
            onSubmit = { onAction(ConversationUiAction.ReportSubmitClicked) }
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
