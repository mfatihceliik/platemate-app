package com.mefy.platemate.presentation.features.main.search

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.performance.StartupTrace
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    onNavigateToSearchDetail: (String) -> Unit,
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

    SearchScreen(
        state = state,
        onAction = viewModel::onAction,
        lazyListState = lazyListState,
        modifier = modifier
    )
}
