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
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailRoute
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailViewModel
import com.mefy.platemate.presentation.features.main.review.ReviewRoute
import com.mefy.platemate.presentation.features.main.review.ReviewViewModel

internal fun NavGraphBuilder.discoverGraph(
    navController: NavHostController,
    onNavigateToDiscoverDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<DiscoverGraphDestination>(startDestination = DiscoverDestination) {
        composable<DiscoverDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            DiscoverRoute(
                viewModel = hiltViewModel<DiscoverViewModel>(parentEntry),
                onNavigateToTrendDetail = onNavigateToDiscoverDetail,
                modifier = modifier
            )
        }

        composable<DiscoverDetailDestination> {
            PlateDetailRoute(
                viewModel = hiltViewModel<PlateDetailViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReview = { navController.navigateToReview(it) },
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
