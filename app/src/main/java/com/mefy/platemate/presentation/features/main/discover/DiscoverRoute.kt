package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverShimmerContent
import com.mefy.platemate.presentation.theme.PMTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DiscoverRoute(
    viewModel: DiscoverViewModel,
    onNavigateToTrendDetail: (String) -> Unit,
    onNavigateToCityPlates: (cityId: Int, cityName: String) -> Unit,
    onNavigateToFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                is DiscoverUiEffect.NavigateToTrendDetail -> {
                    onNavigateToTrendDetail(effect.trendId)
                }
                is DiscoverUiEffect.NavigateToCityPlates -> {
                    onNavigateToCityPlates(effect.cityId, effect.cityName)
                }
            }
        }
    }

    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    val spacing = PMTheme.spacing

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.main_tab_discover),
            alignment = PMTopBarAlignment.Start,
            showBackButton = false
        ),
        viewModel = viewModel,
        status = status,
        isRefreshing = state.isRefreshing,
        loading = { innerPadding ->
            DiscoverShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = spacing.s16, vertical = spacing.s16)
            )
        }) { innerPadding ->
        DiscoverScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            onOpenFilter = onNavigateToFilter,
            lazyListState = lazyListState,
            innerPadding = innerPadding
        )
    }
}
