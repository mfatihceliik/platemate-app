package com.mefy.platemate.presentation.features.main.profile.reviewlist

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileReviewListRoute(
    viewModel: ProfileReviewListViewModel,
    onBackClick: () -> Unit,
    onNavigateToReviewDetail: (String, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ProfileReviewListUiEffect.NavigateBack -> onBackClick()
                is ProfileReviewListUiEffect.NavigateToReviewDetail ->
                    onNavigateToReviewDetail(effect.plateCode, effect.reviewId)
            }
        }
    }

    val onAction = viewModel::onAction
    val onRetry = remember(onAction) { { onAction(ProfileReviewListUiAction.RetryClicked) } }
    val onBack = remember(onAction) { { onAction(ProfileReviewListUiAction.BackClicked) } }

    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        state.errorMessage != null && state.reviews.isEmpty() -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_review_list_title),
            alignment = PMTopBarAlignment.Start,
            onBackClick = onBack
        ),
        status = status,
        onRetry = onRetry
    ) { innerPadding ->
        ProfileReviewListScreen(
            state = state,
            onAction = onAction,
            lazyListState = lazyListState,
            innerPadding = innerPadding
        )
    }
}
