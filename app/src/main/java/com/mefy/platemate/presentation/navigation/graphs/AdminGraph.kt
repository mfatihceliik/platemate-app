package com.mefy.platemate.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.admin.commentreasons.CommentReasonsRoute
import com.mefy.platemate.presentation.features.admin.commentreasons.CommentReasonsViewModel
import com.mefy.platemate.presentation.features.admin.commentreasons.form.CommentReasonFormRoute
import com.mefy.platemate.presentation.features.admin.commentreasons.form.CommentReasonFormViewModel
import com.mefy.platemate.presentation.features.admin.plateremovalreasons.PlateRemovalReasonsRoute
import com.mefy.platemate.presentation.features.admin.plateremovalreasons.PlateRemovalReasonsViewModel
import com.mefy.platemate.presentation.features.admin.plateremovalreasons.form.PlateRemovalReasonFormRoute
import com.mefy.platemate.presentation.features.admin.plateremovalreasons.form.PlateRemovalReasonFormViewModel
import com.mefy.platemate.presentation.features.admin.hub.AdminHubRoute
import com.mefy.platemate.presentation.features.admin.hub.AdminHubViewModel
import com.mefy.platemate.presentation.features.admin.moderation.comments.CommentModerationRoute
import com.mefy.platemate.presentation.features.admin.moderation.comments.CommentModerationViewModel
import com.mefy.platemate.presentation.features.admin.moderation.plates.HiddenPlatesRoute
import com.mefy.platemate.presentation.features.admin.moderation.plates.HiddenPlatesViewModel
import com.mefy.platemate.presentation.features.admin.moderation.removal.PlateRemovalRoute
import com.mefy.platemate.presentation.features.admin.moderation.removal.PlateRemovalViewModel
import com.mefy.platemate.presentation.features.admin.moderation.reports.CommentReportsRoute
import com.mefy.platemate.presentation.features.admin.moderation.reports.CommentReportsViewModel
import com.mefy.platemate.presentation.features.admin.accentcolors.AccentColorsRoute
import com.mefy.platemate.presentation.features.admin.accentcolors.AccentColorsViewModel
import com.mefy.platemate.presentation.features.admin.accentcolors.form.AccentColorFormRoute
import com.mefy.platemate.presentation.features.admin.accentcolors.form.AccentColorFormViewModel
import com.mefy.platemate.presentation.features.admin.premiumfeatures.PremiumFeaturesRoute
import com.mefy.platemate.presentation.features.admin.premiumfeatures.PremiumFeaturesViewModel
import com.mefy.platemate.presentation.features.admin.premiumfeatures.form.PremiumFeatureFormRoute
import com.mefy.platemate.presentation.features.admin.premiumfeatures.form.PremiumFeatureFormViewModel
import com.mefy.platemate.presentation.features.admin.premiumplans.PremiumPlansRoute
import com.mefy.platemate.presentation.features.admin.premiumplans.PremiumPlansViewModel
import com.mefy.platemate.presentation.features.admin.premiumplans.form.PremiumPlanFormRoute
import com.mefy.platemate.presentation.features.admin.premiumplans.form.PremiumPlanFormViewModel
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
        screenComposable<AdminHubDestination, AdminHubViewModel>(
            viewModel = { hiltViewModel<AdminHubViewModel>() },
        ) { viewModel ->
            AdminHubRoute(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() },
                onItemClick = { code ->
                    // Backend menü kodu → Android destination; bilinmeyen kod no-op (forward-compat).
                    val destination: AppDestination? = when (code) {
                        "PENDING_COMMENTS" -> AdminCommentModerationDestination
                        "COMMENT_REPORTS" -> AdminCommentReportsDestination
                        "PLATE_REMOVAL_REQUESTS" -> AdminPlateRemovalDestination
                        "HIDDEN_PLATES" -> AdminHiddenPlatesDestination
                        "PLATE_REPORT_TYPES" -> AdminReportTypesDestination
                        "COMMENT_REPORT_REASONS" -> AdminCommentReasonsDestination
                        "PLATE_REMOVAL_REASONS" -> AdminPlateRemovalReasonsDestination
                        "SOCIAL_PLATFORMS" -> AdminSocialPlatformsDestination
                        "PREMIUM_PLANS" -> AdminPremiumPlansDestination
                        "PREMIUM_FEATURES" -> AdminPremiumFeaturesDestination
                        "THEME_COLORS" -> AdminAccentColorsDestination
                        "APP_SETTINGS" -> AdminSettingsDestination
                        else -> null
                    }
                    destination?.let { navController.navigate(it) }
                },
            )
        }

        screenComposable<AdminCommentModerationDestination, CommentModerationViewModel>(
            viewModel = { hiltViewModel<CommentModerationViewModel>() },
        ) { viewModel ->
            CommentModerationRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminCommentReportsDestination, CommentReportsViewModel>(
            viewModel = { hiltViewModel<CommentReportsViewModel>() },
        ) { viewModel ->
            CommentReportsRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminPlateRemovalDestination, PlateRemovalViewModel>(
            viewModel = { hiltViewModel<PlateRemovalViewModel>() },
        ) { viewModel ->
            PlateRemovalRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminHiddenPlatesDestination, HiddenPlatesViewModel>(
            viewModel = { hiltViewModel<HiddenPlatesViewModel>() },
        ) { viewModel ->
            HiddenPlatesRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminSettingsDestination, AdminSettingsViewModel>(
            viewModel = { hiltViewModel<AdminSettingsViewModel>() },
        ) { viewModel ->
            AdminSettingsRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminReportTypesDestination, ReportTypesViewModel>(
            viewModel = { hiltViewModel<ReportTypesViewModel>() },
        ) { viewModel ->
            ReportTypesRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { typeId ->
                    navController.navigate(AdminReportTypeFormDestination(typeId ?: -1L))
                },
            )
        }

        screenComposable<AdminReportTypeFormDestination, ReportTypeFormViewModel>(
            viewModel = { hiltViewModel<ReportTypeFormViewModel>() },
        ) { viewModel ->
            ReportTypeFormRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminCommentReasonsDestination, CommentReasonsViewModel>(
            viewModel = { hiltViewModel<CommentReasonsViewModel>() },
        ) { viewModel ->
            CommentReasonsRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { reasonId ->
                    navController.navigate(AdminCommentReasonFormDestination(reasonId ?: -1L))
                },
            )
        }

        screenComposable<AdminCommentReasonFormDestination, CommentReasonFormViewModel>(
            viewModel = { hiltViewModel<CommentReasonFormViewModel>() },
        ) { viewModel ->
            CommentReasonFormRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminPlateRemovalReasonsDestination, PlateRemovalReasonsViewModel>(
            viewModel = { hiltViewModel<PlateRemovalReasonsViewModel>() },
        ) { viewModel ->
            PlateRemovalReasonsRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { reasonId ->
                    navController.navigate(AdminPlateRemovalReasonFormDestination(reasonId ?: -1L))
                },
            )
        }

        screenComposable<AdminPlateRemovalReasonFormDestination, PlateRemovalReasonFormViewModel>(
            viewModel = { hiltViewModel<PlateRemovalReasonFormViewModel>() },
        ) { viewModel ->
            PlateRemovalReasonFormRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminSocialPlatformsDestination, SocialPlatformsViewModel>(
            viewModel = { hiltViewModel<SocialPlatformsViewModel>() },
        ) { viewModel ->
            SocialPlatformsRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { platformId ->
                    navController.navigate(AdminSocialPlatformFormDestination(platformId ?: -1L))
                },
            )
        }

        screenComposable<AdminSocialPlatformFormDestination, SocialPlatformFormViewModel>(
            viewModel = { hiltViewModel<SocialPlatformFormViewModel>() },
        ) { viewModel ->
            SocialPlatformFormRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminPremiumPlansDestination, PremiumPlansViewModel>(
            viewModel = { hiltViewModel<PremiumPlansViewModel>() },
        ) { viewModel ->
            PremiumPlansRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { planId ->
                    navController.navigate(AdminPremiumPlanFormDestination(planId))
                },
            )
        }

        screenComposable<AdminPremiumPlanFormDestination, PremiumPlanFormViewModel>(
            viewModel = { hiltViewModel<PremiumPlanFormViewModel>() },
        ) { viewModel ->
            PremiumPlanFormRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminPremiumFeaturesDestination, PremiumFeaturesViewModel>(
            viewModel = { hiltViewModel<PremiumFeaturesViewModel>() },
        ) { viewModel ->
            PremiumFeaturesRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { featureId ->
                    navController.navigate(AdminPremiumFeatureFormDestination(featureId ?: -1L))
                },
            )
        }

        screenComposable<AdminPremiumFeatureFormDestination, PremiumFeatureFormViewModel>(
            viewModel = { hiltViewModel<PremiumFeatureFormViewModel>() },
        ) { viewModel ->
            PremiumFeatureFormRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }

        screenComposable<AdminAccentColorsDestination, AccentColorsViewModel>(
            viewModel = { hiltViewModel<AccentColorsViewModel>() },
        ) { viewModel ->
            AccentColorsRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToForm = { colorId ->
                    navController.navigate(AdminAccentColorFormDestination(colorId ?: -1L))
                },
            )
        }

        screenComposable<AdminAccentColorFormDestination, AccentColorFormViewModel>(
            viewModel = { hiltViewModel<AccentColorFormViewModel>() },
        ) { viewModel ->
            AccentColorFormRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
            )
        }
    }
}
