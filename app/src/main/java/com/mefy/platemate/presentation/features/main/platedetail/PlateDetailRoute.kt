package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.features.main.platedetail.components.PlateDetailShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlateDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: PlateDetailViewModel,
    onNavigateToReview: (String) -> Unit,
    onNavigateToUserProfile: (Long) -> Unit,
    onNavigateToEditReview: (plateCode: String, reviewId: Long) -> Unit,
    onNavigateToActions: (plateCode: String) -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PlateDetailUiEffect.NavigateBack -> navController.navigateUp()
                is PlateDetailUiEffect.NavigateToReview -> onNavigateToReview(effect.plateCode)
                is PlateDetailUiEffect.NavigateToUserProfile -> onNavigateToUserProfile(effect.userId)
                is PlateDetailUiEffect.NavigateToEditReview ->
                    onNavigateToEditReview(effect.plateCode, effect.reviewId)
                is PlateDetailUiEffect.NavigateToActions -> onNavigateToActions(effect.plateCode)
            }
        }
    }

    val onAction = viewModel::onAction
    val onMenuClicked = remember(onAction) { { onAction(PlateDetailUiAction.MenuClicked) } }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.platedetail_title),
            actions = {
                PMIconButton(
                    onClick = onMenuClicked,
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.platedetail_menu)
                )
            }
        ),
        viewModel = viewModel,
        status = status,
        loading = { innerPadding ->
            PlateDetailShimmerContent(
                modifier = Modifier.fillMaxSize()
            )
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
