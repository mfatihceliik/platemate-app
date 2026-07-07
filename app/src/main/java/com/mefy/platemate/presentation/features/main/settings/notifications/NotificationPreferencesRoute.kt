package com.mefy.platemate.presentation.features.main.settings.notifications

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationPreferencesRoute(
    modifier: Modifier = Modifier,
    viewModel: NotificationPreferencesViewModel,
    onBackClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is NotificationPreferencesUiEffect.ShowSnackbar -> {
                    onShowSnackbar(effect.message.resolve(context))
                }
            }
        }
    }

    val onAction = viewModel::onAction
    val dims = MaterialTheme.pmDimensions

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    val onRetry = remember(onAction) { { onAction(NotificationPreferencesUiAction.RetryClicked) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_notification_preferences_title),
            onBackClick = onBackClick
        ),
        status = status,
        onRetry = onRetry,
        loading = { innerPadding -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding)) },
    ) { innerPadding ->
        NotificationPreferencesScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
