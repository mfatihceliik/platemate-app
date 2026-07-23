package com.mefy.platemate.presentation.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.mefy.platemate.presentation.features.main.scanner.CameraScannerRoute
import com.mefy.platemate.presentation.features.main.scanner.ScannerViewModel
import com.mefy.platemate.presentation.features.main.search.SearchRoute
import com.mefy.platemate.presentation.features.main.search.SearchUiAction
import com.mefy.platemate.presentation.features.main.search.SearchViewModel
import com.mefy.platemate.presentation.features.main.search.usersearch.UserSearchViewModel
import com.mefy.platemate.presentation.navigation.CameraScannerDestination
import com.mefy.platemate.presentation.navigation.MainGraphDestination
import com.mefy.platemate.presentation.navigation.PlateActionsDestination
import com.mefy.platemate.presentation.navigation.PlateRemovalRequestDestination
import com.mefy.platemate.presentation.navigation.ReviewDestination
import com.mefy.platemate.presentation.navigation.SearchDestination
import com.mefy.platemate.presentation.navigation.SearchDetailDestination
import com.mefy.platemate.presentation.navigation.SearchGraphDestination
import com.mefy.platemate.presentation.navigation.navigateToCameraScanner
import com.mefy.platemate.presentation.navigation.navigateToEditReview
import com.mefy.platemate.presentation.navigation.navigateToPlateActions
import com.mefy.platemate.presentation.navigation.navigateToRemovalRequest
import com.mefy.platemate.presentation.navigation.navigateToReview
import com.mefy.platemate.presentation.navigation.navigateToUserProfile
import com.mefy.platemate.presentation.navigation.screenComposable

internal fun NavGraphBuilder.searchGraph(
    navController: NavHostController,
    onNavigateToSearchDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<SearchGraphDestination>(startDestination = SearchDestination) {
        screenComposable<SearchDestination, SearchViewModel>(
            viewModel = { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainGraphDestination)
                }
                hiltViewModel<SearchViewModel>(parentEntry)
            },
        ) { searchViewModel ->
            val scannedPlate by backStackEntry.savedStateHandle
                .getStateFlow<String?>("scanned_plate", null)
                .collectAsStateWithLifecycle()

            LaunchedEffect(scannedPlate) {
                if (!scannedPlate.isNullOrEmpty()) {
                    searchViewModel.onAction(SearchUiAction.PlateInputChanged(scannedPlate!!))
                    searchViewModel.onAction(SearchUiAction.SearchClicked)
                    backStackEntry.savedStateHandle.remove<String>("scanned_plate")
                }
            }

            SearchRoute(
                viewModel = searchViewModel,
                userSearchViewModel = hiltViewModel<UserSearchViewModel>(),
                onNavigateToSearchDetail = onNavigateToSearchDetail,
                onNavigateToCameraScanner = { navController.navigateToCameraScanner() },
                onNavigateToUserProfile = { userId -> navController.navigateToUserProfile(userId.toString()) },
                modifier = modifier
            )
        }

        screenComposable<SearchDetailDestination, PlateDetailViewModel>(
            viewModel = { hiltViewModel<PlateDetailViewModel>() },
        ) { viewModel ->
            PlateDetailRoute(
                viewModel = viewModel,

                onNavigateToReview = { navController.navigateToReview(it) },
                onNavigateToUserProfile = { userId -> navController.navigateToUserProfile(userId.toString()) },
                onNavigateToEditReview = { code, reviewId ->
                    navController.navigateToEditReview(code, reviewId)
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

                modifier = modifier
            )
        }

        screenComposable<ReviewDestination, ReviewViewModel>(
            viewModel = { hiltViewModel<ReviewViewModel>() },
        ) { viewModel ->
            ReviewRoute(
                viewModel = viewModel,

                onReviewSubmitted = { navController.popBackStack() },
                modifier = modifier
            )
        }

        // Kamera tarayıcı banner toplamaz (BaseViewModel akışı yok); düz composable.
        composable<CameraScannerDestination> {
            CameraScannerRoute(
                modifier = modifier,
                viewModel = hiltViewModel<ScannerViewModel>(),

                onPlateFound = { plate ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_plate", plate)
                    navController.popBackStack()
                },
            )
        }
    }
}
