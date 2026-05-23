package com.mefy.platemate.presentation.features.auth.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WelcomeRoute(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    WelcomeScreen(
        onCreateAccountClick = onNavigateToRegister,
        onSignInClick = onNavigateToLogin,
        modifier = modifier
    )
}