package com.mefy.platemate.presentation.features.main.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReviewRoute(
    viewModel: ReviewViewModel,
    onNavigateBack: () -> Unit,
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ReviewUiEffect.NavigateBack -> onNavigateBack()
                ReviewUiEffect.ReviewSubmitted -> onReviewSubmitted()
            }
        }
    }

    ReviewScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
