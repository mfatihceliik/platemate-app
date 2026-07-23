package com.mefy.platemate.presentation.features.main.settings.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChangePasswordRoute(
    viewModel: ChangePasswordViewModel,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ChangePasswordUiEffect.NavigateBack -> navController.navigateUp()
                ChangePasswordUiEffect.PasswordChanged -> {
                    onShowSnackbar("Şifre güncellendi")
                    navController.navigateUp()
                }
            }
        }
    }

    val onAction = viewModel::onAction
    val onBack = remember(onAction) { { onAction(ChangePasswordUiAction.BackClicked) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_change_password_title)
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
