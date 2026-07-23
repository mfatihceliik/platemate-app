package com.mefy.platemate.presentation.navigation.graphs

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.main.profile.ProfileRoute
import com.mefy.platemate.presentation.features.main.profile.ProfileViewModel
import com.mefy.platemate.presentation.features.main.profile.followlist.UserFollowListRoute
import com.mefy.platemate.presentation.features.main.profile.followlist.UserFollowListViewModel
import com.mefy.platemate.presentation.features.main.profile.friends.ProfileFriendsRoute
import com.mefy.platemate.presentation.features.main.profile.friends.ProfileFriendsViewModel
import com.mefy.platemate.presentation.features.main.profile.reviewlist.ProfileReviewListRoute
import com.mefy.platemate.presentation.features.main.profile.reviewlist.ProfileReviewListViewModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.UserProfileRoute
import com.mefy.platemate.presentation.features.main.profile.userprofile.UserProfileViewModel
import com.mefy.platemate.presentation.navigation.ChatDestination
import com.mefy.platemate.presentation.navigation.MainGraphDestination
import com.mefy.platemate.presentation.navigation.ProfileDestination
import com.mefy.platemate.presentation.navigation.ProfileFriendsDestination
import com.mefy.platemate.presentation.navigation.ProfileGraphDestination
import com.mefy.platemate.presentation.navigation.ProfileReviewListDestination
import com.mefy.platemate.presentation.navigation.SearchDetailDestination
import com.mefy.platemate.presentation.navigation.UserFollowListDestination
import com.mefy.platemate.presentation.navigation.UserProfileDestination
import com.mefy.platemate.presentation.navigation.navigateToProfileFriends
import com.mefy.platemate.presentation.navigation.navigateToProfileReviewList
import com.mefy.platemate.presentation.navigation.navigateToUserFollowList
import com.mefy.platemate.presentation.navigation.screenComposable

internal fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    onShowSnackbar: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<ProfileGraphDestination>(startDestination = ProfileDestination) {
        screenComposable<ProfileDestination, ProfileViewModel>(
            viewModel = { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainGraphDestination)
                }
                hiltViewModel<ProfileViewModel>(parentEntry)
            },
        ) { viewModel ->
            ProfileRoute(
                viewModel = viewModel,
                onNavigateToReviewDetail = { code, reviewId ->
                    navController.navigate(SearchDetailDestination(id = code, highlightReviewId = reviewId))
                },
                onNavigateToFriends = { tab -> navController.navigateToProfileFriends(tab) },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(UserProfileDestination(userId.toString()))
                },
                onNavigateToReviewList = { status -> navController.navigateToProfileReviewList(status) },
                modifier = modifier
            )
        }

        screenComposable<ProfileReviewListDestination, ProfileReviewListViewModel>(
            viewModel = { hiltViewModel<ProfileReviewListViewModel>() },
        ) { viewModel ->
            ProfileReviewListRoute(
                viewModel = viewModel,

                onNavigateToReviewDetail = { code, reviewId ->
                    navController.navigate(SearchDetailDestination(id = code, highlightReviewId = reviewId))
                },
                modifier = modifier
            )
        }

        screenComposable<UserProfileDestination, UserProfileViewModel>(
            viewModel = { hiltViewModel<UserProfileViewModel>() },
        ) { viewModel ->
            UserProfileRoute(
                viewModel = viewModel,

                onNavigateToChat = { conversationId, otherUserId, participantName ->
                    navController.navigate(
                        ChatDestination(
                            conversationId = conversationId,
                            otherUserId = otherUserId,
                            participantName = participantName,
                        )
                    )
                },
                onNavigateToFollowList = { userId, initialTab ->
                    navController.navigateToUserFollowList(userId, initialTab)
                },
                modifier = modifier
            )
        }

        screenComposable<UserFollowListDestination, UserFollowListViewModel>(
            viewModel = { hiltViewModel<UserFollowListViewModel>() },
        ) { viewModel ->
            UserFollowListRoute(
                viewModel = viewModel,

                onNavigateToUserProfile = { userId ->
                    navController.navigate(UserProfileDestination(userId))
                },
                modifier = modifier
            )
        }

        screenComposable<ProfileFriendsDestination, ProfileFriendsViewModel>(
            viewModel = { hiltViewModel<ProfileFriendsViewModel>() },
        ) { viewModel ->
            ProfileFriendsRoute(
                viewModel = viewModel,

                onNavigateToUserProfile = { userId ->
                    navController.navigate(UserProfileDestination(userId))
                },
                modifier = modifier
            )
        }
    }
}
