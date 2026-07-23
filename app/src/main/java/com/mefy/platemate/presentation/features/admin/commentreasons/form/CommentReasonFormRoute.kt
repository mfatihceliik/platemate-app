package com.mefy.platemate.presentation.features.admin.commentreasons.form

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.theme.PMTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommentReasonFormRoute(
    viewModel: CommentReasonFormViewModel,
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = PMTheme.spacing

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                CommentReasonFormUiEffect.NavigateBack -> navController.navigateUp()
            }
        }
    }

    val titleRes = if (state.isEdit) R.string.admin_comment_reason_edit_title else R.string.admin_comment_reason_add_title

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(titleRes)
        ),
        status = if (state.isLoading) ScreenStatus.Loading else ScreenStatus.Content,
        contentPadding = PaddingValues(spacing.s16),
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) },
    ) { innerPadding ->
        CommentReasonFormScreen(
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
