package com.mefy.platemate.presentation.features.main.profile.followlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.LaunchedEffect

@Composable
fun UserFollowListRoute(
    modifier: Modifier = Modifier,
    viewModel: UserFollowListViewModel,
    onNavigateToUserProfile: (String) -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                UserFollowListUiEffect.NavigateBack -> navController.navigateUp()
                is UserFollowListUiEffect.NavigateToUserProfile -> onNavigateToUserProfile(effect.userId)
            }
        }
    }

    val title = if (state.selectedTab == 0) {
        stringResource(R.string.user_follow_list_followers_tab)
    } else {
        stringResource(R.string.user_follow_list_following_tab)
    }

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = title,
            alignment = PMTopBarAlignment.Start,
            onBackClick = { viewModel.onAction(UserFollowListUiAction.BackClicked) }
        ),
        viewModel = viewModel,
        status = status,
        loading = { innerPadding ->
            PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding))
        }
    ) { innerPadding ->
        UserFollowListScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
