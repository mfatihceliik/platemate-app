package com.mefy.platemate.presentation.features.main.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.features.main.profile.components.ProfileShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel,
    onNavigateToSearchDetail: (String) -> Unit,
    onNavigateToFriends: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is ProfileUiEffect.NavigateToSearchDetail -> {
                    onNavigateToSearchDetail(effect.normalizedPlateCode)
                }

                ProfileUiEffect.NavigateToFriends -> {
                    onNavigateToFriends()
                }

                is ProfileUiEffect.ShowSnackbar -> Unit
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(ProfileUiAction.OnResume)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val onAction = viewModel::onAction
    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    val onRetry = remember(onAction) { { onAction(ProfileUiAction.RetryClicked) } }
    val onRefresh = remember(onAction) { { onAction(ProfileUiAction.RefreshRequested) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.main_tab_profile),
            alignment = PMTopBarAlignment.Start
        ),
        status = status,
        onRetry = onRetry,
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        loading = { innerPadding ->
            ProfileShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    ) { innerPadding ->
        ProfileScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            lazyListState = lazyListState,
            innerPadding = innerPadding
        )
    }
}
