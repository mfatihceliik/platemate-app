package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.main.settings.ProfileSettingsRoute
import com.mefy.platemate.presentation.features.main.settings.ProfileSettingsViewModel
import com.mefy.platemate.presentation.features.main.settings.changepassword.ChangePasswordRoute
import com.mefy.platemate.presentation.features.main.settings.changepassword.ChangePasswordViewModel
import com.mefy.platemate.presentation.features.main.settings.editprofile.EditProfileRoute
import com.mefy.platemate.presentation.features.main.settings.editprofile.EditProfileViewModel
import com.mefy.platemate.presentation.features.main.settings.language.LanguageRoute
import com.mefy.platemate.presentation.features.main.settings.language.LanguageViewModel
import com.mefy.platemate.presentation.features.main.settings.notifications.NotificationPreferencesRoute
import com.mefy.platemate.presentation.features.main.settings.notifications.NotificationPreferencesViewModel
import com.mefy.platemate.presentation.features.main.settings.premium.PremiumInfoRoute
import com.mefy.platemate.presentation.features.main.settings.premium.PremiumInfoViewModel
import com.mefy.platemate.presentation.features.main.settings.sociallinks.SocialLinksRoute
import com.mefy.platemate.presentation.features.main.settings.sociallinks.SocialLinksViewModel
import com.mefy.platemate.presentation.features.main.settings.themecolor.ThemeColorRoute
import com.mefy.platemate.presentation.features.main.settings.themecolor.ThemeColorViewModel

internal fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    onShowSnackbar: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<SettingsGraphDestination>(startDestination = ProfileSettingsHomeDestination) {
        // Sekme kökü: geri butonu yok (onBackClick = null).
        composable<ProfileSettingsHomeDestination> {
            ProfileSettingsRoute(
                viewModel = hiltViewModel<ProfileSettingsViewModel>(),
                onBackClick = null,
                onNavigateToChangePassword = { navController.navigateToProfileChangePassword() },
                onNavigateToEditProfile = { navController.navigateToEditProfile() },
                onNavigateToPremium = { navController.navigateToProfilePremiumInfo() },
                onNavigateToThemeColor = { navController.navigateToProfileThemeColor() },
                onNavigateToLanguage = { navController.navigateToProfileLanguage() },
                onNavigateToNotificationPreferences = { navController.navigateToProfileNotificationPreferences() },
                onNavigateToSocialLinks = { navController.navigateToProfileSocialLinks() },
                modifier = modifier
            )
        }

        composable<EditProfileDestination> {
            EditProfileRoute(
                viewModel = hiltViewModel<EditProfileViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
                modifier = modifier
            )
        }

        composable<ProfileChangePasswordDestination> {
            ChangePasswordRoute(
                viewModel = hiltViewModel<ChangePasswordViewModel>(),
                onBackClick = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
                modifier = modifier
            )
        }

        composable<ProfileThemeColorDestination> {
            ThemeColorRoute(
                viewModel = hiltViewModel<ThemeColorViewModel>(),
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable<ProfileLanguageDestination> {
            LanguageRoute(
                viewModel = hiltViewModel<LanguageViewModel>(),
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable<ProfileNotificationPreferencesDestination> {
            NotificationPreferencesRoute(
                viewModel = hiltViewModel<NotificationPreferencesViewModel>(),
                onBackClick = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
                modifier = modifier
            )
        }

        composable<ProfilePremiumInfoDestination> {
            PremiumInfoRoute(
                viewModel = hiltViewModel<PremiumInfoViewModel>(),
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable<ProfileSocialLinksDestination> {
            SocialLinksRoute(
                viewModel = hiltViewModel<SocialLinksViewModel>(),
                onBackClick = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
                modifier = modifier
            )
        }
    }
}
