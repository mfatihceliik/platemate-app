package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.features.main.platedetail.review.components.ReviewShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReviewRoute(
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel,
    onReviewSubmitted: () -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ReviewUiEffect.NavigateBack -> navController.navigateUp()
                ReviewUiEffect.ReviewSubmitted -> onReviewSubmitted()
            }
        }
    }

    val onAction = viewModel::onAction

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(if (state.isEditMode) R.string.review_edit_title else R.string.review_title)
        ),
        applyImePadding = true,
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
