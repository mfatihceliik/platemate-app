package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import androidx.compose.material3.MaterialTheme
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.features.main.platedetail.review.components.ReviewShimmerContent
import com.mefy.platemate.presentation.theme.pmDimensions
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

    val onAction = viewModel::onAction
    val dims = MaterialTheme.pmDimensions

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(if (state.isEditMode) R.string.review_edit_title else R.string.review_title),
            onBackClick = { onAction(ReviewUiAction.BackClicked) }),
        applyImePadding = true,
        contentPadding = PaddingValues(dims.spacing.s16),
        loading = { innerPadding ->
            ReviewShimmerContent(
                modifier = Modifier.fillMaxSize()
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
