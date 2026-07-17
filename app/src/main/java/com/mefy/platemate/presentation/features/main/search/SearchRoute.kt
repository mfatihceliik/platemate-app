package com.mefy.platemate.presentation.features.main.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMTabRow
import com.mefy.platemate.presentation.components.PMTabItem
import com.mefy.platemate.presentation.features.main.search.components.SearchShimmerContent
import com.mefy.platemate.presentation.features.main.search.usersearch.UserSearchScreen
import com.mefy.platemate.presentation.features.main.search.usersearch.UserSearchUiEffect
import com.mefy.platemate.presentation.features.main.search.usersearch.UserSearchViewModel
import com.mefy.platemate.presentation.performance.StartupTrace
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    userSearchViewModel: UserSearchViewModel,
    onNavigateToSearchDetail: (String) -> Unit,
    onNavigateToCameraScanner: () -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val userSearchState by userSearchViewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

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

    LaunchedEffect(userSearchViewModel) {
        userSearchViewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is UserSearchUiEffect.NavigateToUserProfile -> onNavigateToUserProfile(effect.userId)
            }
        }
    }

    val onAction = viewModel::onAction

    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.main_tab_search),
            alignment = PMTopBarAlignment.Start,
            onBackClick = null
        ),
        status = status,
        loading = { innerPadding ->
            SearchShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        },
        onRetry = { onAction(SearchUiAction.RetryClicked) }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            PMTabRow(
                selectedTabIndex = selectedTab,
                tabs = listOf(
                    PMTabItem(
                        title = stringResource(R.string.search_tab_plate),
                        icon = Icons.Filled.DirectionsCar
                    ),
                    PMTabItem(
                        title = stringResource(R.string.search_tab_users),
                        icon = Icons.Filled.Person
                    )
                ),
                onTabSelected = { selectedTab = it }
            )
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Box(modifier = if (selectedTab == 0) Modifier.matchParentSize() else Modifier.size(0.dp)) {
                    SearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onAction = viewModel::onAction,
                        onNavigateToCameraScanner = onNavigateToCameraScanner,
                        lazyListState = lazyListState,
                        innerPadding = innerPadding
                    )
                }
                Box(modifier = if (selectedTab == 1) Modifier.matchParentSize() else Modifier.size(0.dp)) {
                    UserSearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        state = userSearchState,
                        onAction = userSearchViewModel::onAction,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}
