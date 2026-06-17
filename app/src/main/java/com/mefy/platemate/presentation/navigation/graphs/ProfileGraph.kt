package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.main.profile.ProfileRoute
import com.mefy.platemate.presentation.features.main.profile.ProfileViewModel
import com.mefy.platemate.presentation.features.main.profile.friends.ProfileFriendsRoute
import com.mefy.platemate.presentation.features.main.profile.friends.ProfileFriendsViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.notifications.NotificationPreferencesRoute
import com.mefy.platemate.presentation.features.main.profile.settings.notifications.NotificationPreferencesViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.premium.PremiumInfoRoute
import com.mefy.platemate.presentation.features.main.profile.settings.premium.PremiumInfoViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.ProfileSettingsRoute
import com.mefy.platemate.presentation.features.main.profile.settings.ProfileSettingsViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.sociallinks.SocialLinksRoute
import com.mefy.platemate.presentation.features.main.profile.settings.sociallinks.SocialLinksViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.changepassword.ChangePasswordRoute
import com.mefy.platemate.presentation.features.main.profile.settings.changepassword.ChangePasswordViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.editprofile.EditProfileRoute
import com.mefy.platemate.presentation.features.main.profile.settings.editprofile.EditProfileViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.language.LanguageRoute
import com.mefy.platemate.presentation.features.main.profile.settings.language.LanguageViewModel
import com.mefy.platemate.presentation.features.main.profile.settings.themecolor.ThemeColorRoute
import com.mefy.platemate.presentation.features.main.profile.settings.themecolor.ThemeColorViewModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.UserProfileRoute
import com.mefy.platemate.presentation.features.main.profile.userprofile.UserProfileViewModel

internal fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    onNavigateToSearchDetail: (String) -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<ProfileGraphDestination>(startDestination = ProfileDestination) {
        composable<ProfileDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            ProfileRoute(
                viewModel = hiltViewModel<ProfileViewModel>(parentEntry),
                onNavigateToSearchDetail = onNavigateToSearchDetail,
                onNavigateToSettings = { navController.navigateToProfileSettings() },
                onNavigateToFriends = { navController.navigateToProfileFriends() },
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

        composable<UserProfileDestination> {
            UserProfileRoute(
                viewModel = hiltViewModel<UserProfileViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = { userId ->
                    // TODO: navigate to chat with userId when messaging supports it
                },
                modifier = modifier
            )
        }

        composable<ProfileSettingsHomeDestination> {
            ProfileSettingsRoute(
                viewModel = hiltViewModel<ProfileSettingsViewModel>(),
                onBackClick = { navController.popBackStack() },
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

        composable<ProfileFriendsDestination> {
            ProfileFriendsRoute(
                viewModel = hiltViewModel<ProfileFriendsViewModel>(),
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}
