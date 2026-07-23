package com.mefy.platemate.presentation.features.auth.login

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPasswordField
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.auth.components.AuthHeroHeader
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginScreenUiState,
    onAction: (LoginUiAction) -> Unit,
    onNavigateToRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val stroke = PMTheme.stroke
    val isSubmitLoading = state.isLoading

    val showEmailValidationError = !state.isEmailFormatValid && (state.hasSubmittedOnce || state.email.isNotBlank())
    val isErrorState = showEmailValidationError || state.emailError != null || state.passwordError != null

    val resolvedEmailError = state.emailError ?: if (showEmailValidationError) {
        stringResource(R.string.auth_login_email_invalid)
    } else {
        null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = spacing.s32),
        verticalArrangement = Arrangement.Top
    ) {
        AuthHeroHeader(
            badgeText = "",
            title = stringResource(R.string.auth_login_hero_title),
            subtitle = stringResource(R.string.auth_login_hero_subtitle)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.s24),
            verticalArrangement = Arrangement.spacedBy(spacing.s16)
        ) {
            PMTextField(
                value = state.email,
                onValueChange = { onAction(LoginUiAction.EmailChanged(it)) },
                label = stringResource(R.string.auth_login_email_label),
                placeholder = stringResource(R.string.auth_login_email_placeholder),
                isError = isErrorState, // highlight if any error to match design
                errorText = resolvedEmailError,
                leadingIcon = {
                    PMIcon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = colors.outline,
                        modifier = Modifier.size(spacing.s16)
                    )
                },
                trailingIcon = if (isErrorState) {
                    {
                        Box(
                            modifier = Modifier
                                .size(spacing.s24)
                                .background(colors.error, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            PMIcon(
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

            PMPasswordField(
                value = state.password,
                onValueChange = { onAction(LoginUiAction.PasswordChanged(it)) },
                label = stringResource(R.string.auth_login_password_label),
                placeholder = stringResource(R.string.auth_login_password_placeholder),
                isError = isErrorState,
                errorText = state.passwordError,
                leadingIcon = {
                    PMIcon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = colors.outline,
                        modifier = Modifier.size(spacing.s16)
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = { onAction(LoginUiAction.SubmitClicked) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                PMText(
                    text = stringResource(R.string.auth_login_forgot_password),
                    style = PMTextStyle.Caption,
                    color = colors.primary,
                    modifier = Modifier.clickable { /* Handle forgot password */ }
                )
            }

            // Login CTA
            PMButton(
                text = stringResource(R.string.auth_login_submit),
                onClick = { onAction(LoginUiAction.SubmitClicked) },
                enabled = state.isSubmitEnabled,
                loading = isSubmitLoading,
                modifier = Modifier.fillMaxWidth()
            )

            // Divider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.s4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.s12)
            ) {
                Box(modifier = Modifier.weight(1f).height(stroke.st1).background(colors.outlineVariant))
                PMText(
                    text = stringResource(R.string.auth_login_or),
                    style = PMTextStyle.Caption,
                    color = colors.outline
                )
                Box(modifier = Modifier.weight(1f).height(stroke.st1).background(colors.outlineVariant))
            }

            // Apple Sign In
            PMButton(
                text = stringResource(R.string.auth_login_apple_continue),
                onClick = { /* Apple Sign In */ },
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0B0F17),
                    contentColor = Color.White
                ),
                leadingIcon = { AppleIcon() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Register nudge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing.s16),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                PMText(
                    text = stringResource(R.string.auth_login_no_account) + " ",
                    style = PMTextStyle.Caption,
                    color = colors.tertiary
                )
                PMText(
                    text = stringResource(R.string.auth_login_register_link),
                    style = PMTextStyle.Caption,
                    color = colors.primary,
                    modifier = Modifier.clickable(onClick = onNavigateToRegisterClick)
                )
            }
        }
    }
}

@Composable
fun AppleIcon() {
    // Simple wrapper for Apple Icon vector path
    val applePathData = "M13.769 10.5c-.027-3.18 2.6-4.72 2.72-4.794-1.484-2.168-3.79-2.464-4.607-2.494-1.956-.196-3.827 1.15-4.818 1.15-.988 0-2.51-1.127-4.13-1.097-2.117.032-4.077 1.23-5.17 3.115-2.205 3.816-.565 9.473 1.584 12.574 1.053 1.52 2.308 3.225 3.957 3.163 1.594-.063 2.196-1.024 4.12-1.024 1.924 0 2.473 1.024 4.155.99 1.715-.03 2.8-1.544 3.843-3.072a17.1 17.1 0 0 0 1.74-3.527c-2.374-.97-2.746-4.16-.394-5.984zM11.013 1.993c.878-1.059 1.47-2.525 1.308-3.993-1.267.052-2.796.844-3.703 1.906-.812.937-1.528 2.448-1.336 3.883 1.407.11 2.845-.712 3.73-1.796z"
    val builder = ImageVector.Builder(
        name = "Apple",
        defaultWidth = 17.dp,
        defaultHeight = 20.dp,
        viewportWidth = 17f,
        viewportHeight = 20f
    )
    val pathNodes = PathParser().parsePathString(applePathData).toNodes()
    builder.addPath(
        pathData = pathNodes,
        fill = SolidColor(Color.White)
    )
    PMIcon(
        imageVector = builder.build(),
        contentDescription = "Apple",
        tint = Color.White,
        modifier = Modifier.size(17.dp, 20.dp)
    )
}

@Preview(name = "Login Screen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
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
                password = "123",
                isSubmitEnabled = true
            ),
            onAction = {},
            onNavigateToRegisterClick = {},
            onBackClick = {}
        )
    }
}
