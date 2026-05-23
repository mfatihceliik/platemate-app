package com.mefy.platemate.presentation.features.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMPasswordField
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.PMTextStyle
import com.mefy.platemate.presentation.features.auth.components.AuthHeroHeader
import com.mefy.platemate.presentation.features.auth.components.AuthTopBar
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun LoginScreen(
    state: LoginScreenUiState,
    onAction: (LoginUiAction) -> Unit,
    onNavigateToRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    val radius = dimensions.radius
    val isSubmitLoading = state.submitState is UiActionState.Loading

    val showEmailValidationError = !state.isEmailFormatValid && (state.hasSubmittedOnce || state.email.isNotBlank())
    val resolvedEmailError = state.emailError ?: if (showEmailValidationError) {
        stringResource(R.string.auth_login_email_invalid)
    } else {
        null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(spacing.s2)
        ) {
            AuthTopBar(
                title = stringResource(R.string.auth_login_screen_title),
                onBackClick = onBackClick,
                modifier = Modifier.padding(top = spacing.s6)
            )

            AuthHeroHeader(
                badgeText = stringResource(R.string.auth_login_hero_badge),
                title = stringResource(R.string.auth_login_hero_title),
                subtitle = stringResource(R.string.auth_login_hero_subtitle)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s14)
        ) {
            PMTextField(
                value = state.email,
                onValueChange = { onAction(LoginUiAction.EmailChanged(it)) },
                label = stringResource(R.string.auth_login_email_label),
                placeholder = stringResource(R.string.auth_login_email_placeholder),
                isError = resolvedEmailError != null,
                errorText = resolvedEmailError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            PMPasswordField(
                value = state.password,
                onValueChange = { onAction(LoginUiAction.PasswordChanged(it)) },
                label = stringResource(R.string.auth_login_password_label),
                placeholder = stringResource(R.string.auth_login_password_placeholder),
                isError = state.passwordError != null,
                errorText = state.passwordError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = { onAction(LoginUiAction.SubmitClicked) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            state.formMessage?.let { formMessage ->
                FormMessage(
                    formMessage = formMessage,
                    isError = state.submitState is UiActionState.Error
                )
            }

            Button(
                onClick = { onAction(LoginUiAction.SubmitClicked) },
                enabled = state.isSubmitEnabled && !isSubmitLoading,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(radius.r14),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = stringResource(R.string.auth_login_submit),
                        style = PMTextStyle.Label,
                        color = colorScheme.onPrimary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.s8),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = stringResource(R.string.auth_login_no_account),
                    style = PMTextStyle.Body,
                    color = colorScheme.onSurfaceVariant
                )
                PMText(
                    text = stringResource(R.string.auth_login_register_link),
                    style = PMTextStyle.Body,
                    color = colorScheme.primary,
                    modifier = Modifier
                        .padding(start = spacing.s6)
                        .clickable(onClick = onNavigateToRegisterClick)
                )
            }
        }
    }
}

@Composable
private fun FormMessage(formMessage: UiText, isError: Boolean) {
    PMText(
        text = formMessage.resolve(),
        style = PMTextStyle.Caption,
        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    )
}

@Preview(name = "Login Screen Light", showBackground = true, backgroundColor = 0xFFF3F6FF)
@Composable
private fun LoginScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        LoginScreen(
            state = LoginScreenUiState(
                email = "ornek@mail.com",
                password = "123456",
                isSubmitEnabled = true
            ),
            onAction = {},
            onNavigateToRegisterClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Login Screen Dark", showBackground = true, backgroundColor = 0xFF07153A)
@Composable
private fun LoginScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        LoginScreen(
            state = LoginScreenUiState(
                email = "ornek@mail.com",
                password = "123456",
                isSubmitEnabled = true
            ),
            onAction = {},
            onNavigateToRegisterClick = {},
            onBackClick = {}
        )
    }
}
