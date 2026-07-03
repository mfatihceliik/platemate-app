package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ConversationEmptyState
import com.mefy.platemate.presentation.features.main.messages.conversation.components.ConversationTopBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.MessageInputBar
import com.mefy.platemate.presentation.features.main.messages.conversation.components.MessageRequestBanner
import com.mefy.platemate.presentation.features.main.settings.ProfileSettingsUiAction
import com.mefy.platemate.presentation.theme.pmColors
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ConversationRoute(
    viewModel: ConversationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChatDetail: (conversationId: String, participantName: String, initials: String, avatarBgArgb: Long, avatarFgArgb: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ConversationUiEffect.NavigateBack -> onNavigateBack()
                is ConversationUiEffect.NavigateToChatDetail -> onNavigateToChatDetail(
                    effect.conversationId,
                    effect.participantName,
                    effect.initials,
                    effect.avatarBgArgb,
                    effect.avatarFgArgb
                )
            }
        }
    }


    val onAction = viewModel::onAction

    // Okundu bildirimi yalnızca ekran gerçekten görünürken atılsın (arka planda "görüldü" gitmesin).
    LifecycleResumeEffect(Unit) {
        onAction(ConversationUiAction.ScreenResumed)
        onPauseOrDispose { onAction(ConversationUiAction.ScreenPaused) }
    }

    val onBackClicked = remember(onAction) { { onAction(ConversationUiAction.BackClicked) } }
    val onInfoClicked = remember(onAction) { { onAction(ConversationUiAction.InfoClicked) } }
    val onRetryClicked = remember(onAction) { { onAction(ConversationUiAction.RetryClicked) } }

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.items.isEmpty() -> ScreenStatus.Empty
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Custom {
            ConversationTopBar(
                modifier = Modifier.wrapContentSize(),
                participantName = state.participantName,
                initials = state.initials,
                avatarBg = state.avatarBg,
                avatarFg = state.avatarFg,
                onBackClick = onBackClicked,
                onInfoClick = onInfoClicked,
                isOnline = state.isOtherUserOnline
            )
        },
        containerColor = MaterialTheme.pmColors.surface,
        applyImePadding = true,
        status = status,
        onRetry = onRetryClicked,
        loading = { innerPadding -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding)) },
        empty = { innerPadding -> ConversationEmptyState(modifier = Modifier.padding(innerPadding)) },
        bottomBar = {
            if (state.errorMessage == null) {
                Column {
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
                        onSend = { onAction(ConversationUiAction.SendClicked) }
                    )
                }
            }
        }
    ) { innerPadding ->
        ConversationScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
