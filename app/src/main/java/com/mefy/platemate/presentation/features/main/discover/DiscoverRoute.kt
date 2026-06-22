package com.mefy.platemate.presentation.features.main.discover

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    DiscoverScreen(
        state = state,
        onAction = viewModel::onAction,
        lazyListState = lazyListState,
        modifier = modifier
    )
}
