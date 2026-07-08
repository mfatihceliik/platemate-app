package com.mefy.platemate.presentation.features.main.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.features.main.search.components.SearchEmptyState
import com.mefy.platemate.presentation.features.main.search.components.SearchShimmerContent
import com.mefy.platemate.presentation.performance.StartupTrace
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    onNavigateToSearchDetail: (String) -> Unit,
    onNavigateToCameraScanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        StartupTrace.markSearchRouteEntered()
        withFrameNanos {
            StartupTrace.markSearchFirstFrameRendered()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is SearchUiEffect.NavigateToSearchDetail -> {
                    onNavigateToSearchDetail(effect.id)
                }
            }
        }
    }

    val onAction = viewModel::onAction
    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.recentSearches.isEmpty() && state.bookmarkedPlates.isEmpty() && state.alarmPlates.isEmpty() -> ScreenStatus.Empty
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.search_header_title),
            alignment = PMTopBarAlignment.Start
        ),
        status = status,
        loading = { innerPadding ->
            SearchShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        },
        empty = { innerPadding ->
            SearchEmptyState()
        },
        onRetry = { onAction(SearchUiAction.RetryClicked) }
    ) { innerPadding ->
        SearchScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            onNavigateToCameraScanner = onNavigateToCameraScanner,
            lazyListState = lazyListState,
            innerPadding = innerPadding
        )
    }
}
