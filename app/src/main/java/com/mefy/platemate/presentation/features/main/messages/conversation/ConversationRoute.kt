package com.mefy.platemate.presentation.features.main.messages.conversation

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    ConversationScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
