package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DiscoverRoute(
    viewModel: DiscoverViewModel,
    onNavigateToTrendDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)
    val lazyListState = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                is DiscoverUiEffect.NavigateToTrendDetail -> {
                    onNavigateToTrendDetail(effect.trendId)
                }
            }
        }
    }

    val onAction = viewModel::onAction

    val onRetry = remember(onAction) { { onAction(DiscoverUiAction.RetryClicked) } }
    val onRefresh = remember(onAction) { { onAction(DiscoverUiAction.RefreshRequested) } }

    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier, topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.discover_header_title),
            alignment = PMTopBarAlignment.Start
        ),
        status = status,
        onRetry = onRetry,
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        loading = { innerPadding ->
            DiscoverShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }) { innerPadding ->
        DiscoverScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            lazyListState = lazyListState,
            innerPadding = innerPadding
        )
    }
}
