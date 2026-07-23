package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ConversationEmptyState
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ConversationTopBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.MessageActionMenu
import com.mefy.platemate.presentation.features.main.messages.conversation.components.MessageInputBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.MessageRequestBanner
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ConversationRoute(
    modifier: Modifier = Modifier,
    viewModel: ConversationViewModel,
    onNavigateToChatDetail: (
        conversationId: String,
        participantName: String,
            ) -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ConversationUiEffect.NavigateBack -> navController.navigateUp()
                is ConversationUiEffect.NavigateToChatDetail -> onNavigateToChatDetail(
                    effect.conversationId,
                    effect.participantName,
                )
            }
        }
    }


    val onAction = viewModel::onAction

    LifecycleResumeEffect(Unit) {
        onAction(ConversationUiAction.ScreenResumed)
        onPauseOrDispose { onAction(ConversationUiAction.ScreenPaused) }
    }

    val onBackClicked = remember(onAction) { { onAction(ConversationUiAction.BackClicked) } }
    val onInfoClicked = remember(onAction) { { onAction(ConversationUiAction.InfoClicked) } }

    val density = LocalDensity.current
    var composerHeightPx by remember { mutableStateOf(with(density) { 80.dp.roundToPx() }) }
    val composerInset = with(density) { composerHeightPx.toDp() }

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.items.isEmpty() -> ScreenStatus.Empty
        else -> ScreenStatus.Content
    }

    var actionMenuAnchor by remember { mutableStateOf<Pair<ChatMessageUiModel, Rect>?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Custom {
            ConversationTopBar(
                modifier = Modifier.wrapContentSize(),
                participantName = state.participantName,
                onBackClick = onBackClicked,
                onInfoClick = onInfoClicked,
                isOnline = state.isOtherUserOnline
            )
        },
        applyImePadding = true,
        viewModel = viewModel,
        status = status,
        loading = { innerPadding -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding)) },
        empty = { innerPadding -> ConversationEmptyState(modifier = Modifier.padding(innerPadding)) },
        contentBottomOverlay = {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .onSizeChanged { composerHeightPx = it.height }
            ) {
                if (state.isIncomingRequest) {
                    MessageRequestBanner(
                        enabled = !state.isRespondingRequest,
                        onAccept = { onAction(ConversationUiAction.AcceptRequestClicked) },
                        onDecline = { onAction(ConversationUiAction.DeclineRequestClicked) }
                    )
                }
                MessageInputBar(
                    text = state.inputText,
                    onTextChange = { onAction(ConversationUiAction.InputChanged(it)) },
                    onSend = { onAction(ConversationUiAction.SendClicked) },
                    replyTarget = state.replyTarget,
                    onCancelReply = { onAction(ConversationUiAction.CancelReplyClicked) }
                )
            }
        }
    ) { innerPadding ->
        ConversationScreen(
            state = state,
            onAction = viewModel::onAction,
            onMessageLongPressed = { message, bounds ->
                actionMenuAnchor = message to bounds
                onAction(ConversationUiAction.MessageLongPressed(message.id))
            },
            innerPadding = innerPadding,
            bottomOverlayInset = composerInset
        )
    }

        val anchor = actionMenuAnchor
        if (state.actionMenuTargetId != null && anchor != null && anchor.first.id == state.actionMenuTargetId) {
            MessageActionMenu(
                message = anchor.first,
                participantName = state.participantName,
                anchorBounds = anchor.second,
                onDismiss = {
                    actionMenuAnchor = null
                    onAction(ConversationUiAction.ActionMenuDismissed)
                },
                onQuoteClick = {
                    onAction(ConversationUiAction.QuoteMessageClicked(anchor.first.id))
                    actionMenuAnchor = null
                },
                onReportClick = {
                    onAction(ConversationUiAction.ReportMessageClicked(anchor.first.id))
                    actionMenuAnchor = null
                },
                onDeleteClick = {
                    onAction(ConversationUiAction.DeleteActionClicked(anchor.first.id))
                    actionMenuAnchor = null
                }
            )
        }
    }
}
