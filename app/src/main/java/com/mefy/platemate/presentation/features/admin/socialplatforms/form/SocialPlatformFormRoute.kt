package com.mefy.platemate.presentation.features.admin.socialplatforms.form

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SocialPlatformFormRoute(
    viewModel: SocialPlatformFormViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                SocialPlatformFormUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    val titleRes = if (state.isEdit) R.string.admin_social_platform_edit_title else R.string.admin_social_platform_add_title

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(titleRes),
            onBackClick = { viewModel.onAction(SocialPlatformFormUiAction.BackClicked) }
        ),
        status = if (state.isLoading) ScreenStatus.Loading else ScreenStatus.Content,
        contentPadding = PaddingValues(MaterialTheme.pmDimensions.spacing.s16),
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) },
    ) { innerPadding ->
        SocialPlatformFormScreen(
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
