package com.mefy.platemate.presentation.features.admin.commentreasons

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
import com.mefy.platemate.presentation.components.PMIconButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommentReasonsRoute(
    viewModel: CommentReasonsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToForm: (reasonId: Long?) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    // Reload whenever the screen resumes (e.g. returning from the add/edit form).
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                CommentReasonsUiEffect.NavigateBack -> onNavigateBack()
                is CommentReasonsUiEffect.NavigateToForm -> onNavigateToForm(effect.reasonId)
            }
        }
    }

    val onAction = viewModel::onAction

    val onBackClicked = remember(onAction) { { onAction(CommentReasonsUiAction.BackClicked) } }
    val onAddClicked = remember(onAction) { { onAction(CommentReasonsUiAction.AddClicked) } }
    val onRetryClicked = remember(onAction) { { onAction(CommentReasonsUiAction.RetryClicked) } }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_comment_reasons_title),
            onBackClick = onBackClicked,
            actions = {
                PMIconButton(
                    onClick = onAddClicked,
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.admin_comment_reason_add)
                )
            }
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = onRetryClicked,
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        CommentReasonsScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}
