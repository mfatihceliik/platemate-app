package com.mefy.platemate.presentation.features.admin.reporttypes

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReportTypesRoute(
    viewModel: ReportTypesViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToForm: (typeId: Long?) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    // Reload whenever the screen resumes (e.g. returning from the add/edit form).
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ReportTypesUiEffect.NavigateBack -> onNavigateBack()
                is ReportTypesUiEffect.NavigateToForm -> onNavigateToForm(effect.typeId)
            }
        }
    }

    val onAction = viewModel::onAction

    val onBackClicked = remember(onAction) { { onAction(ReportTypesUiAction.BackClicked) } }
    val onAddClicked = remember(onAction) { { onAction(ReportTypesUiAction.AddClicked) } }
    val onRetryClicked = remember(onAction) { { onAction(ReportTypesUiAction.RetryClicked) } }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_report_types_title),
            onBackClick = onBackClicked,
            actions = {
                PMIconButton(
                    onClick = onAddClicked,
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.admin_report_type_add)
                )
            }
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = onRetryClicked,
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        ReportTypesScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}
