package com.mefy.platemate.presentation.features.admin.premiumplans

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PremiumPlansRoute(
    viewModel: PremiumPlansViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToForm: (planId: Long) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PremiumPlansUiEffect.NavigateBack -> onNavigateBack()
                is PremiumPlansUiEffect.NavigateToForm -> onNavigateToForm(effect.planId)
            }
        }
    }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_premium_plans_title),
            onBackClick = { viewModel.onAction(PremiumPlansUiAction.BackClicked) }
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = { viewModel.onAction(PremiumPlansUiAction.RetryClicked) },
        contentPadding = PaddingValues(MaterialTheme.pmDimensions.spacing.s16),
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        PremiumPlansScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}
