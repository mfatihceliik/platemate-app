package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.features.main.platedetail.review.components.ReviewShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReviewRoute(
    viewModel: ReviewViewModel,
    onNavigateBack: () -> Unit,
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ReviewUiEffect.NavigateBack -> onNavigateBack()
                ReviewUiEffect.ReviewSubmitted -> onReviewSubmitted()
            }
        }
    }

    val onAction = viewModel::onAction

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(if (state.isEditMode) R.string.review_edit_title else R.string.review_title),
            onBackClick = { onAction(ReviewUiAction.BackClicked) }),
        loading = { innerPadding ->
            ReviewShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        },
    ) { innerPadding ->
        ReviewScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
