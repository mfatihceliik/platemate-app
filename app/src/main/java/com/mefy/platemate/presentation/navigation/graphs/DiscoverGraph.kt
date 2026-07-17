package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.main.discover.DiscoverRoute
import com.mefy.platemate.presentation.features.main.discover.DiscoverViewModel
import com.mefy.platemate.presentation.features.main.discover.cityplates.CityPlatesRoute
import com.mefy.platemate.presentation.features.main.discover.cityplates.CityPlatesViewModel
import com.mefy.platemate.presentation.features.main.discover.filter.DiscoverCityFilterRoute
import com.mefy.platemate.presentation.features.main.discover.filter.DiscoverFilterRoute
import com.mefy.platemate.presentation.features.main.discover.filter.DiscoverRatingFilterRoute
import com.mefy.platemate.presentation.features.main.discover.filter.DiscoverReportTypeFilterRoute
import com.mefy.platemate.presentation.features.main.discover.filter.DiscoverWindowFilterRoute
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailRoute
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailViewModel
import com.mefy.platemate.presentation.features.main.platedetail.actions.PlateActionsRoute
import com.mefy.platemate.presentation.features.main.platedetail.actions.PlateActionsViewModel
import com.mefy.platemate.presentation.features.main.platedetail.removal.PlateRemovalRequestRoute
import com.mefy.platemate.presentation.features.main.platedetail.removal.PlateRemovalRequestViewModel
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewRoute
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewViewModel

internal fun NavGraphBuilder.discoverGraph(
    navController: NavHostController,
    onNavigateToDiscoverDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<DiscoverGraphDestination>(startDestination = DiscoverDestination) {
        screenComposable<DiscoverDestination, DiscoverViewModel>(
            viewModel = { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainGraphDestination)
                }
                hiltViewModel<DiscoverViewModel>(parentEntry)
            },
        ) { viewModel ->
            DiscoverRoute(
                viewModel = viewModel,
                onNavigateToTrendDetail = onNavigateToDiscoverDetail,
                onNavigateToCityPlates = { cityId, cityName ->
                    navController.navigateToDiscoverCityPlates(cityId, cityName)
                },
                onNavigateToFilter = { navController.navigateToDiscoverFilter() },
                modifier = modifier
            )
        }

        // Filtre ekranları DiscoverViewModel'i paylaşır ama kendi banner'ını toplamaz; düz composable.
        composable<DiscoverFilterDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            DiscoverFilterRoute(
                viewModel = hiltViewModel<DiscoverViewModel>(parentEntry),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCityFilter = { navController.navigateToDiscoverCityFilter() },
                onNavigateToRatingFilter = { navController.navigateToDiscoverRatingFilter() },
                onNavigateToReportTypeFilter = { navController.navigateToDiscoverReportTypeFilter() },
                onNavigateToWindowFilter = { navController.navigateToDiscoverWindowFilter() },
                onNavigateToPremiumInfo = { navController.navigateToProfilePremiumInfo() },
                modifier = modifier
            )
        }

        composable<DiscoverCityFilterDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            DiscoverCityFilterRoute(
                viewModel = hiltViewModel<DiscoverViewModel>(parentEntry),
                onNavigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable<DiscoverRatingFilterDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            DiscoverRatingFilterRoute(
                viewModel = hiltViewModel<DiscoverViewModel>(parentEntry),
                onNavigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable<DiscoverReportTypeFilterDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            DiscoverReportTypeFilterRoute(
                viewModel = hiltViewModel<DiscoverViewModel>(parentEntry),
                onNavigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable<DiscoverWindowFilterDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            DiscoverWindowFilterRoute(
                viewModel = hiltViewModel<DiscoverViewModel>(parentEntry),
                onNavigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }

        screenComposable<DiscoverCityPlatesDestination, CityPlatesViewModel>(
            viewModel = { hiltViewModel<CityPlatesViewModel>() },
        ) { viewModel ->
            CityPlatesRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlateDetail = onNavigateToDiscoverDetail,
                modifier = modifier
            )
        }

        screenComposable<DiscoverDetailDestination, PlateDetailViewModel>(
            viewModel = { hiltViewModel<PlateDetailViewModel>() },
        ) { viewModel ->
            PlateDetailRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReview = { navController.navigateToReview(it) },
                onNavigateToUserProfile = { userId -> navController.navigateToUserProfile(userId.toString()) },
                onNavigateToEditReview = { code, reviewId, rating, comment ->
                    navController.navigateToEditReview(code, reviewId, rating, comment)
                },
                onNavigateToActions = { plateCode -> navController.navigateToPlateActions(plateCode) },
                modifier = modifier
            )
        }

        screenComposable<PlateActionsDestination, PlateActionsViewModel>(
            viewModel = { hiltViewModel<PlateActionsViewModel>() },
        ) { viewModel ->
            PlateActionsRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRemoval = { plateId, plateCode ->
                    navController.navigateToRemovalRequest(plateId, plateCode)
                },
                modifier = modifier
            )
        }

        screenComposable<PlateRemovalRequestDestination, PlateRemovalRequestViewModel>(
            viewModel = { hiltViewModel<PlateRemovalRequestViewModel>() },
        ) { viewModel ->
            PlateRemovalRequestRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }

        screenComposable<ReviewDestination, ReviewViewModel>(
            viewModel = { hiltViewModel<ReviewViewModel>() },
        ) { viewModel ->
            ReviewRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onReviewSubmitted = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}
