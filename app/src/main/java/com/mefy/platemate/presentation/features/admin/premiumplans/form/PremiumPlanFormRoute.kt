package com.mefy.platemate.presentation.features.admin.premiumplans.form

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PremiumPlanFormRoute(
    viewModel: PremiumPlanFormViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PremiumPlanFormUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_premium_plan_edit_title),
            onBackClick = { viewModel.onAction(PremiumPlanFormUiAction.BackClicked) }
        ),
        status = if (state.isLoading) ScreenStatus.Loading else ScreenStatus.Content,
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) },
    ) { innerPadding ->
        PremiumPlanFormScreen(
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
