package com.mefy.platemate.presentation.navigation.graphs

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
import com.mefy.platemate.presentation.features.main.profile.userprofile.UserProfileRoute
import com.mefy.platemate.presentation.features.main.profile.userprofile.UserProfileViewModel
import com.mefy.platemate.presentation.navigation.ChatDestination
import com.mefy.platemate.presentation.navigation.MainGraphDestination
import com.mefy.platemate.presentation.navigation.ProfileDestination
import com.mefy.platemate.presentation.navigation.ProfileFriendsDestination
import com.mefy.platemate.presentation.navigation.ProfileGraphDestination
import com.mefy.platemate.presentation.navigation.UserProfileDestination
import com.mefy.platemate.presentation.navigation.navigateToProfileFriends

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
                onNavigateToFriends = { navController.navigateToProfileFriends() },
                modifier = modifier
            )
        }

        composable<UserProfileDestination> {
            UserProfileRoute(
                viewModel = hiltViewModel<UserProfileViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = { conversationId, otherUserId, participantName ->
                    navController.navigate(
                        ChatDestination(
                            conversationId = conversationId,
                            otherUserId = otherUserId,
                            participantName = participantName,
                        )
                    )
                },
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
