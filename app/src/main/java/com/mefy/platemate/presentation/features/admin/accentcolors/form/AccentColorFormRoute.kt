package com.mefy.platemate.presentation.features.admin.accentcolors.form

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
fun AccentColorFormRoute(
    viewModel: AccentColorFormViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                AccentColorFormUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    val titleRes = if (state.isEdit) R.string.admin_theme_color_edit_title else R.string.admin_theme_color_add_title

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(titleRes),
            onBackClick = { viewModel.onAction(AccentColorFormUiAction.BackClicked) }
        ),
        status = if (state.isLoading) ScreenStatus.Loading else ScreenStatus.Content,
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) },
    ) { innerPadding ->
        AccentColorFormScreen(
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
