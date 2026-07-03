package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.admin.hub.AdminHubRoute
import com.mefy.platemate.presentation.features.admin.moderation.comments.CommentModerationRoute
import com.mefy.platemate.presentation.features.admin.moderation.comments.CommentModerationViewModel
import com.mefy.platemate.presentation.features.admin.moderation.plates.HiddenPlatesRoute
import com.mefy.platemate.presentation.features.admin.moderation.plates.HiddenPlatesViewModel
import com.mefy.platemate.presentation.features.admin.moderation.removal.PlateRemovalRoute
import com.mefy.platemate.presentation.features.admin.moderation.removal.PlateRemovalViewModel
import com.mefy.platemate.presentation.features.admin.moderation.reports.CommentReportsRoute
import com.mefy.platemate.presentation.features.admin.moderation.reports.CommentReportsViewModel
import com.mefy.platemate.presentation.features.admin.reporttypes.ReportTypesRoute
import com.mefy.platemate.presentation.features.admin.reporttypes.ReportTypesViewModel
import com.mefy.platemate.presentation.features.admin.reporttypes.form.ReportTypeFormRoute
import com.mefy.platemate.presentation.features.admin.reporttypes.form.ReportTypeFormViewModel
import com.mefy.platemate.presentation.features.admin.settings.AdminSettingsRoute
import com.mefy.platemate.presentation.features.admin.settings.AdminSettingsViewModel
import com.mefy.platemate.presentation.features.admin.socialplatforms.SocialPlatformsRoute
import com.mefy.platemate.presentation.features.admin.socialplatforms.SocialPlatformsViewModel
import com.mefy.platemate.presentation.features.admin.socialplatforms.form.SocialPlatformFormRoute
import com.mefy.platemate.presentation.features.admin.socialplatforms.form.SocialPlatformFormViewModel

internal fun NavGraphBuilder.adminGraph(
    navController: NavHostController,
) {
    navigation<AdminGraphDestination>(startDestination = AdminHubDestination) {
        composable<AdminHubDestination> {
            AdminHubRoute(
                onBackClick = { navController.navigateUp() },
                onPendingComments = { navController.navigate(AdminCommentModerationDestination) },
                onCommentReports = { navController.navigate(AdminCommentReportsDestination) },
                onPlateRemoval = { navController.navigate(AdminPlateRemovalDestination) },
                onHiddenPlates = { navController.navigate(AdminHiddenPlatesDestination) },
                onReportTypes = { navController.navigate(AdminReportTypesDestination) },
                onSocialPlatforms = { navController.navigate(AdminSocialPlatformsDestination) },
                onSettings = { navController.navigate(AdminSettingsDestination) },
            )
        }

        composable<AdminCommentModerationDestination> {
            CommentModerationRoute(
                viewModel = hiltViewModel<CommentModerationViewModel>(),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AdminCommentReportsDestination> {
            CommentReportsRoute(
                viewModel = hiltViewModel<CommentReportsViewModel>(),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AdminPlateRemovalDestination> {
            PlateRemovalRoute(
                viewModel = hiltViewModel<PlateRemovalViewModel>(),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AdminHiddenPlatesDestination> {
            HiddenPlatesRoute(
                viewModel = hiltViewModel<HiddenPlatesViewModel>(),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AdminSettingsDestination> {
            AdminSettingsRoute(
                viewModel = hiltViewModel<AdminSettingsViewModel>(),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AdminReportTypesDestination> {
            ReportTypesRoute(
                viewModel = hiltViewModel<ReportTypesViewModel>(),
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { typeId ->
                    navController.navigate(AdminReportTypeFormDestination(typeId ?: -1L))
                },
            )
        }

        composable<AdminReportTypeFormDestination> {
            ReportTypeFormRoute(
                viewModel = hiltViewModel<ReportTypeFormViewModel>(),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AdminSocialPlatformsDestination> {
            SocialPlatformsRoute(
                viewModel = hiltViewModel<SocialPlatformsViewModel>(),
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { platformId ->
                    navController.navigate(AdminSocialPlatformFormDestination(platformId ?: -1L))
                },
            )
        }

        composable<AdminSocialPlatformFormDestination> {
            SocialPlatformFormRoute(
                viewModel = hiltViewModel<SocialPlatformFormViewModel>(),
                onNavigateBack = { navController.navigateUp() },
            )
        }
    }
}
