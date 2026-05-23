package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.MainPlaceholderScreen

internal fun NavGraphBuilder.messagesGraph(
    modifier: Modifier = Modifier
) {
    navigation<MessagesGraphDestination>(startDestination = MessagesDestination) {
        composable<MessagesDestination> {
            MainPlaceholderScreen(
                titleRes = R.string.main_messages_placeholder_title,
                modifier = modifier
            )
        }

        // TODO: Add typed deep links if/when this detail screen is implemented.
        composable<ChatDestination> {
            MainPlaceholderScreen(
                titleRes = R.string.main_chat_placeholder_title,
                modifier = modifier
            )
        }
    }
}
