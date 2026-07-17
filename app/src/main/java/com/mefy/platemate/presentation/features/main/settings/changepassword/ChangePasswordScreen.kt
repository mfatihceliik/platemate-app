package com.mefy.platemate.presentation.features.main.settings.changepassword

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMPasswordField
import com.mefy.platemate.presentation.features.main.settings.changepassword.components.ValidationChecklist
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    state: ChangePasswordUiState,
    onAction: (ChangePasswordUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val onSubmit = remember(onAction) { { onAction(ChangePasswordUiAction.SubmitClicked) } }
    val onCurrentChange = remember(onAction) { { v: String -> onAction(ChangePasswordUiAction.CurrentPasswordChanged(v)) } }
    val onNewChange = remember(onAction) { { v: String -> onAction(ChangePasswordUiAction.NewPasswordChanged(v)) } }
    val onConfirmChange = remember(onAction) { { v: String -> onAction(ChangePasswordUiAction.ConfirmPasswordChanged(v)) } }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = spacedByWithFooter(dims.spacing.s8)
    ) {
        item {
            PMSectionLabel(text = stringResource(R.string.profile_change_password_desc))
        }

        item {
            PMPasswordField(
                value = state.currentPassword,
                onValueChange = onCurrentChange,
                label = stringResource(R.string.profile_change_password_current),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            PMPasswordField(
                value = state.newPassword,
                onValueChange = onNewChange,
                label = stringResource(R.string.profile_change_password_new),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            PMPasswordField(
                value = state.confirmPassword,
                onValueChange = onConfirmChange,
                label = stringResource(R.string.profile_change_password_confirm),
                placeholder = stringResource(R.string.profile_change_password_confirm_placeholder),
                isError = state.confirmPassword.isNotEmpty() && !state.passwordsMatch,
                errorText = if (state.confirmPassword.isNotEmpty() && !state.passwordsMatch) {
                    stringResource(R.string.profile_change_password_error_mismatch)
                } else null,
                showToggle = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            ValidationChecklist(state = state)
        }
        
        item {
            PMButton(
                text = stringResource(R.string.profile_change_password_submit),
                onClick = onSubmit,
                enabled = state.isSubmitEnabled,
                loading = state.isSubmitting,
                modifier = Modifier.fillMaxWidth().padding(vertical = dims.spacing.s16)
            )
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
            onAction = {},
        )
    }
}
