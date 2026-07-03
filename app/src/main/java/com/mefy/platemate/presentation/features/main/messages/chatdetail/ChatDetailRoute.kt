package com.mefy.platemate.presentation.features.main.messages.chatdetail

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatDetailRoute(
    viewModel: ChatDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
    onNavigateToMessagesList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ChatDetailUiEffect.NavigateBack -> onNavigateBack()
                is ChatDetailUiEffect.NavigateToUserProfile -> onNavigateToUserProfile(effect.userId)
                ChatDetailUiEffect.NavigateToMessagesList -> onNavigateToMessagesList()
            }
        }
    }

    ChatDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
