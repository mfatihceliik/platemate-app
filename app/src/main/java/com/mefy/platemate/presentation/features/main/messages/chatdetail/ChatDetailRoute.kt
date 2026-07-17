package com.mefy.platemate.presentation.features.main.messages.chatdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
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

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ChatDetailUiEffect.NavigateBack -> onNavigateBack()
                is ChatDetailUiEffect.NavigateToUserProfile -> onNavigateToUserProfile(effect.userId)
                ChatDetailUiEffect.NavigateToMessagesList -> onNavigateToMessagesList()
            }
        }
    }

    val onAction = viewModel::onAction
    val onBackClicked = remember(onAction) { { onAction(ChatDetailUiAction.BackClicked) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.chatdetail_title),
            onBackClick = onBackClicked
        ),
    ) { innerPadding ->
        ChatDetailScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
