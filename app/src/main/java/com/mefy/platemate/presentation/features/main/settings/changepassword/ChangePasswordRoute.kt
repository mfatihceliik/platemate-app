package com.mefy.platemate.presentation.features.main.settings.changepassword

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChangePasswordRoute(
    viewModel: ChangePasswordViewModel,
    onBackClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ChangePasswordUiEffect.NavigateBack -> onBackClick()
                ChangePasswordUiEffect.PasswordChanged -> {
                    onShowSnackbar("Şifre güncellendi")
                    onBackClick()
                }
            }
        }
    }

    val onAction = viewModel::onAction
    val onBack = remember(onAction) { { onAction(ChangePasswordUiAction.BackClicked) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_change_password_title),
            onBackClick = onBack
        ),
    ) { innerPadding ->
        ChangePasswordScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
