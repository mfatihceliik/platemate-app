package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.MainPlaceholderScreen

internal fun NavGraphBuilder.profileGraph(
    modifier: Modifier = Modifier
) {
    navigation<ProfileGraphDestination>(startDestination = ProfileDestination) {
        composable<ProfileDestination> {
            MainPlaceholderScreen(
                titleRes = R.string.main_profile_placeholder_title,
                modifier = modifier
            )
        }

        // TODO: Add typed deep links if/when this detail screen is implemented.
        composable<EditProfileDestination> {
            MainPlaceholderScreen(
                titleRes = R.string.main_edit_profile_placeholder_title,
                modifier = modifier
            )
        }
    }
}
