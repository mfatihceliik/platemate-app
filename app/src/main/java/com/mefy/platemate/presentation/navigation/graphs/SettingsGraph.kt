package com.mefy.platemate.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.main.settings.SettingsRoute
import com.mefy.platemate.presentation.features.main.settings.SettingsViewModel
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
import com.mefy.platemate.presentation.features.main.settings.cardstyle.CardStyleRoute
import com.mefy.platemate.presentation.features.main.settings.cardstyle.CardStyleViewModel
import com.mefy.platemate.presentation.features.main.settings.themecolor.ThemeColorRoute
import com.mefy.platemate.presentation.features.main.settings.themecolor.ThemeColorViewModel

internal fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    onShowSnackbar: (String) -> Unit = {},
) {
    navigation<SettingsGraphDestination>(startDestination = SettingsHomeDestination) {
        // Sekme kökü: geri butonu yok (onBackClick = null).
        screenComposable<SettingsHomeDestination, SettingsViewModel>(
            viewModel = { hiltViewModel<SettingsViewModel>() },
        ) { viewModel ->
            SettingsRoute(
                viewModel = viewModel,

                onNavigateToChangePassword = { navController.navigateToProfileChangePassword() },
                onNavigateToEditProfile = { navController.navigateToEditProfile() },
                onNavigateToPremium = { navController.navigateToProfilePremiumInfo() },
                onNavigateToThemeColor = { navController.navigateToProfileThemeColor() },
                onNavigateToCardStyle = { navController.navigateToProfileCardStyle() },
                onNavigateToLanguage = { navController.navigateToProfileLanguage() },
                onNavigateToNotificationPreferences = { navController.navigateToProfileNotificationPreferences() },
                onNavigateToAdmin = { navController.navigate(AdminHubDestination) },
            )
        }

        screenComposable<EditProfileDestination, EditProfileViewModel>(
            viewModel = { hiltViewModel<EditProfileViewModel>() },
        ) { viewModel ->
            EditProfileRoute(
                viewModel = viewModel,

                onShowSnackbar = onShowSnackbar,
            )
        }

        screenComposable<ProfileChangePasswordDestination, ChangePasswordViewModel>(
            viewModel = { hiltViewModel<ChangePasswordViewModel>() },
        ) { viewModel ->
            ChangePasswordRoute(
                viewModel = viewModel,

                onShowSnackbar = onShowSnackbar,
            )
        }

        screenComposable<ProfileThemeColorDestination, ThemeColorViewModel>(
            viewModel = { hiltViewModel<ThemeColorViewModel>() },
        ) { viewModel ->
            ThemeColorRoute(
                viewModel = viewModel,

            )
        }

        screenComposable<ProfileCardStyleDestination, CardStyleViewModel>(
            viewModel = { hiltViewModel<CardStyleViewModel>() },
        ) { viewModel ->
            CardStyleRoute(
                viewModel = viewModel,

                onNavigateToPremiumInfo = { navController.navigateToProfilePremiumInfo() },
            )
        }

        screenComposable<ProfileLanguageDestination, LanguageViewModel>(
            viewModel = { hiltViewModel<LanguageViewModel>() },
        ) { viewModel ->
            LanguageRoute(
                viewModel = viewModel,

            )
        }

        screenComposable<ProfileNotificationPreferencesDestination, NotificationPreferencesViewModel>(
            viewModel = { hiltViewModel<NotificationPreferencesViewModel>() },
        ) { viewModel ->
            NotificationPreferencesRoute(
                viewModel = viewModel,

                onShowSnackbar = onShowSnackbar,
            )
        }

        screenComposable<ProfilePremiumInfoDestination, PremiumInfoViewModel>(
            viewModel = { hiltViewModel<PremiumInfoViewModel>() },
        ) { viewModel ->
            PremiumInfoRoute(
                viewModel = viewModel,

            )
        }
    }
}
