package com.mefy.platemate.presentation.features.main.profile.friends

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.runtime.LaunchedEffect
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMEmptyState

@Composable
fun ProfileFriendsRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileFriendsViewModel,
    onNavigateToUserProfile: (String) -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ProfileFriendsUiEffect.NavigateBack -> navController.navigateUp()
                is ProfileFriendsUiEffect.NavigateToUserProfile -> onNavigateToUserProfile(effect.userId)
            }
        }
    }

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.friends.isEmpty() && state.pendingRequests.isEmpty() && state.sentRequests.isEmpty() -> ScreenStatus.Empty
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_friends_title),
            alignment = PMTopBarAlignment.Start,
            onBackClick = { viewModel.onAction(ProfileFriendsUiAction.BackClicked) }
        ),
        viewModel = viewModel,
        status = status,
        loading = { innerPadding ->
            PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding))
        },
        empty = { innerPadding ->
            PMEmptyState(
                icon = Icons.Outlined.PeopleOutline,
                message = stringResource(R.string.profile_friends_empty_global),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    ) { innerPadding ->
        ProfileFriendsScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
