package com.mefy.platemate.presentation.features.main.profile.settings.changepassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMPasswordField
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.profile.settings.changepassword.components.ValidationChecklist
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ChangePasswordScreen(
    state: ChangePasswordUiState,
    onAction: (ChangePasswordUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_change_password_title),
            onBackClick = { onAction(ChangePasswordUiAction.BackClicked) }
        ),
        containerColor = colors.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
            ) {
                PMButton(
                    text = stringResource(R.string.profile_change_password_submit),
                    onClick = { onAction(ChangePasswordUiAction.SubmitClicked) },
                    enabled = state.isSubmitEnabled,
                    loading = state.isSubmitting,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            PMText(
                text = stringResource(R.string.profile_change_password_desc),
                color = colors.textSecondary,
            )

            PMPasswordField(
                value = state.currentPassword,
                onValueChange = { onAction(ChangePasswordUiAction.CurrentPasswordChanged(it)) },
                label = stringResource(R.string.profile_change_password_current),
                modifier = Modifier.fillMaxWidth()
            )

            PMPasswordField(
                value = state.newPassword,
                onValueChange = { onAction(ChangePasswordUiAction.NewPasswordChanged(it)) },
                label = stringResource(R.string.profile_change_password_new),
                modifier = Modifier.fillMaxWidth()
            )

            PMPasswordField(
                value = state.confirmPassword,
                onValueChange = { onAction(ChangePasswordUiAction.ConfirmPasswordChanged(it)) },
                label = stringResource(R.string.profile_change_password_confirm),
                placeholder = stringResource(R.string.profile_change_password_confirm_placeholder),
                isError = state.confirmPassword.isNotEmpty() && !state.passwordsMatch,
                errorText = if (state.confirmPassword.isNotEmpty() && !state.passwordsMatch) {
                    stringResource(R.string.profile_change_password_error_mismatch)
                } else null,
                showToggle = false,
                modifier = Modifier.fillMaxWidth()
            )

            ValidationChecklist(state = state)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ChangePasswordScreen(
            state = ChangePasswordUiState(
                currentPassword = "12345678",
                newPassword = "NewPass1",
                confirmPassword = ""
            ),
            onAction = {}
        )
    }
}
