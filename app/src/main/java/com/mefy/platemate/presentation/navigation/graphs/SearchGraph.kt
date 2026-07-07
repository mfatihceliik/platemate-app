package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailRoute
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailViewModel
import com.mefy.platemate.presentation.features.main.platedetail.actions.PlateActionsRoute
import com.mefy.platemate.presentation.features.main.platedetail.actions.PlateActionsViewModel
import com.mefy.platemate.presentation.features.main.platedetail.removal.PlateRemovalRequestRoute
import com.mefy.platemate.presentation.features.main.platedetail.removal.PlateRemovalRequestViewModel
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewRoute
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewViewModel
import com.mefy.platemate.presentation.features.main.search.SearchRoute
import com.mefy.platemate.presentation.features.main.search.SearchViewModel

internal fun NavGraphBuilder.searchGraph(
    navController: NavHostController,
    onNavigateToSearchDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<SearchGraphDestination>(startDestination = SearchDestination) {
        composable<SearchDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            SearchRoute(
                viewModel = hiltViewModel<SearchViewModel>(parentEntry),
                onNavigateToSearchDetail = onNavigateToSearchDetail,
                modifier = modifier
            )
        }

        composable<SearchDetailDestination> {
            PlateDetailRoute(
                viewModel = hiltViewModel<PlateDetailViewModel>(),
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

        composable<PlateActionsDestination> {
            PlateActionsRoute(
                viewModel = hiltViewModel<PlateActionsViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRemoval = { plateId, plateCode ->
                    navController.navigateToRemovalRequest(plateId, plateCode)
                },
                modifier = modifier
            )
        }

        composable<PlateRemovalRequestDestination> {
            PlateRemovalRequestRoute(
                viewModel = hiltViewModel<PlateRemovalRequestViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable<ReviewDestination> {
            ReviewRoute(
                viewModel = hiltViewModel<ReviewViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onReviewSubmitted = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}
