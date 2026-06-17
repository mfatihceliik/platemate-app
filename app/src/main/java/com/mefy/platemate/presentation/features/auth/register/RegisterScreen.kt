package com.mefy.platemate.presentation.features.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMPasswordField
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.auth.components.AuthHeroHeader
import com.mefy.platemate.presentation.features.auth.components.AuthInfoBanner
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun RegisterScreen(
    state: RegisterScreenUiState,
    onAction: (RegisterUiAction) -> Unit,
    onNavigateToLoginClick: () -> Unit,
    onBackClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    val radius = dimensions.radius
    val stroke = dimensions.stroke
    val isSubmitLoading = state.submitState is UiActionState.Loading

    val showEmailValidationError = !state.isEmailFormatValid && (state.hasSubmittedOnce || state.email.isNotBlank())
    val showPasswordValidationError = !state.isPasswordLengthValid && (state.hasSubmittedOnce || state.password.isNotBlank())
    val isSubmitError = state.submitState is UiActionState.Error

    val isEmailErrorState = showEmailValidationError || state.emailError != null || isSubmitError
    val isPasswordErrorState = showPasswordValidationError || state.passwordError != null

    val resolvedEmailError = state.emailError ?: if (showEmailValidationError) {
        stringResource(R.string.auth_register_email_invalid)
    } else {
        null
    }
    val resolvedPasswordError = state.passwordError ?: if (showPasswordValidationError) {
        stringResource(R.string.auth_register_password_min_length, state.passwordMinLength)
    } else {
        null
    }
    
    // Simulate valid username check for design:
    val isUsernameValid = state.username.length >= 3 && state.usernameError == null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = spacing.s32),
        verticalArrangement = Arrangement.Top
    ) {
        AuthHeroHeader(
            badgeText = "",
            title = stringResource(R.string.auth_register_hero_title),
            subtitle = stringResource(R.string.auth_register_hero_subtitle)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.s24),
            verticalArrangement = Arrangement.spacedBy(spacing.s16)
        ) {
            // Email
            PMTextField(
                value = state.email,
                onValueChange = { onAction(RegisterUiAction.EmailChanged(it)) },
                label = stringResource(R.string.auth_register_email_label),
                placeholder = stringResource(R.string.auth_register_email_placeholder),
                isError = isEmailErrorState,
                errorText = resolvedEmailError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = colorScheme.outline,
                        modifier = Modifier.size(spacing.s16)
                    )
                },
                trailingIcon = if (isEmailErrorState) {
                    {
                        Box(
                            modifier = Modifier
                                .size(spacing.s16)
                                .background(colorScheme.error, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(spacing.s12)
                            )
                        }
                    }
                } else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Username
            PMTextField(
                value = state.username,
                onValueChange = { onAction(RegisterUiAction.UsernameChanged(it)) },
                label = stringResource(R.string.auth_register_username_label),
                placeholder = stringResource(R.string.auth_register_username_placeholder),
                isError = state.usernameError != null,
                errorText = state.usernameError,
                isSuccess = isUsernameValid && state.username.isNotEmpty(),
                supportingText = if (isUsernameValid && state.username.isNotEmpty()) stringResource(R.string.auth_register_username_available) else null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = colorScheme.outline,
                        modifier = Modifier.size(spacing.s16)
                    )
                },
                trailingIcon = if (isUsernameValid && state.username.isNotEmpty()) {
                    {
                        Box(
                            modifier = Modifier
                                .size(spacing.s16)
                                .background(colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(spacing.s12)
                            )
                        }
                    }
                } else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Password
            Column(verticalArrangement = Arrangement.spacedBy(spacing.s4)) {
                PMPasswordField(
                    value = state.password,
                    onValueChange = { onAction(RegisterUiAction.PasswordChanged(it)) },
                    label = stringResource(R.string.auth_register_password_label),
                    placeholder = stringResource(R.string.auth_register_password_placeholder),
                    isError = isPasswordErrorState,
                    errorText = resolvedPasswordError,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = colorScheme.outline,
                            modifier = Modifier.size(spacing.s16)
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onDone = { onAction(RegisterUiAction.SubmitClicked) }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Password Strength Indicator
                PasswordStrengthIndicator(
                    strength = state.passwordStrength,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(spacing.s8))

            AuthInfoBanner(
                text = stringResource(R.string.auth_register_terms_info),
                links = listOf(
                    stringResource(R.string.auth_register_terms_link1),
                    stringResource(R.string.auth_register_terms_link2)
                )
            )


            PMButton(
                text = stringResource(R.string.auth_register_submit),
                onClick = { onAction(RegisterUiAction.SubmitClicked) },
                enabled = state.isSubmitEnabled,
                loading = isSubmitLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing.s16),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                PMText(
                    text = stringResource(R.string.auth_register_have_account) + " ",
                    style = PMTextStyle.Caption,
                    color = colorScheme.onSurfaceVariant
                )
                PMText(
                    text = stringResource(R.string.auth_register_sign_in_link),
                    style = PMTextStyle.Caption,
                    color = colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onNavigateToLoginClick)
                )
            }
        }
    }
}

@Preview(name = "Register Screen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun RegisterScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        RegisterScreen(
            state = RegisterScreenUiState(
                username = "ahmet_yl",
                email = "example@gmail.com",
                password = "123",
                hasSubmittedOnce = true,
                passwordStrength = PasswordStrength(
                    level = PasswordStrengthLevel.WEAK,
                    progress = 0.34f
                ),
                isSubmitEnabled = true
            ),
            onAction = {},
            onNavigateToLoginClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Register Screen Dark", showBackground = true, backgroundColor = 0xFF07153A)
@Composable
private fun RegisterScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        RegisterScreen(
            state = RegisterScreenUiState(
                username = "mfatihceliik",
                email = "example@gmail.com",
                password = "Strong123!",
                passwordStrength = PasswordStrength(
                    level = PasswordStrengthLevel.STRONG,
                    progress = 0.9f
                ),
                isSubmitEnabled = true
            ),
            onAction = {},
            onNavigateToLoginClick = {},
            onBackClick = {}
        )
    }
}
