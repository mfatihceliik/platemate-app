package com.mefy.platemate.presentation.features.auth.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.features.auth.model.OnboardingStep
import com.mefy.platemate.presentation.features.auth.onboarding.components.FeaturePagerContent
import com.mefy.platemate.presentation.features.auth.onboarding.components.NotificationPermissionContent
import com.mefy.platemate.presentation.features.auth.onboarding.components.SplashContent

@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by rememberSaveable { mutableStateOf(OnboardingStep.SPLASH) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            onNavigateToRegister()
        }
    )

    Crossfade(
        targetState = currentStep,
        label = "OnboardingCrossfade",
        // App Scaffold artık inset eklemiyor (contentWindowInsets = 0); onboarding kendi
        // güvenli alanını yönetir — Login/Register ile aynı desen.
        modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { step ->
        when (step) {
            OnboardingStep.SPLASH -> {
                SplashContent(
                    onStartClick = { currentStep = OnboardingStep.FEATURES },
                    onLoginClick = onNavigateToLogin
                )
            }
            OnboardingStep.FEATURES -> {
                FeaturePagerContent(
                    onSkipClick = { currentStep = OnboardingStep.PERMISSION },
                    onRegisterClick = { currentStep = OnboardingStep.PERMISSION }
                )
            }
            OnboardingStep.PERMISSION -> {
                NotificationPermissionContent(
                    onAllowClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            onNavigateToRegister()
                        }
                    },
                    onSkipClick = onNavigateToRegister
                )
            }
        }
    }
}
