package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.features.main.platedetail.components.EmptyPlateState
import com.mefy.platemate.presentation.features.main.platedetail.components.PlateDetailShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlateDetailRoute(
    viewModel: PlateDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReview: (String) -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
    onNavigateToEditReview: (plateCode: String, reviewId: Long, rating: Int, comment: String) -> Unit,
    onNavigateToActions: (plateCode: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PlateDetailUiEffect.NavigateBack -> onNavigateBack()
                is PlateDetailUiEffect.NavigateToReview -> onNavigateToReview(effect.plateCode)
                is PlateDetailUiEffect.NavigateToUserProfile -> onNavigateToUserProfile(effect.userId)
                is PlateDetailUiEffect.NavigateToEditReview ->
                    onNavigateToEditReview(effect.plateCode, effect.reviewId, effect.rating, effect.comment)
                is PlateDetailUiEffect.NavigateToActions -> onNavigateToActions(effect.plateCode)
            }
        }
    }

    val onAction = viewModel::onAction
    val onBackClicked = remember(onAction) { { onAction(PlateDetailUiAction.BackClicked) } }
    val onMenuClicked = remember(onAction) { { onAction(PlateDetailUiAction.MenuClicked) } }
    val onRetryClicked = remember(onAction) { { onAction(PlateDetailUiAction.RetryClicked) } }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.platedetail_title),
            onBackClick = onBackClicked,
            actions = {
                PMIconButton(
                    onClick = onMenuClicked,
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.platedetail_menu)
                )
            }
        ),
        status = status,
        onRetry = onRetryClicked,
        loading = { innerPadding ->
            PlateDetailShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        },
        empty = { innerPadding ->
            EmptyPlateState()
        },
    ) { innerPadding ->
        PlateDetailScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
