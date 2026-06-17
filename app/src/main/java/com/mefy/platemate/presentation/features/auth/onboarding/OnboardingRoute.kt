package com.mefy.platemate.presentation.features.auth.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnboardingRoute(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingScreen(
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier
    )
}
