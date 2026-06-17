package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.compose.runtime.remember
import com.mefy.platemate.presentation.features.main.messages.MessagesRoute
import com.mefy.platemate.presentation.features.main.messages.MessagesViewModel
import com.mefy.platemate.presentation.features.main.messages.chatdetail.ChatDetailRoute
import com.mefy.platemate.presentation.features.main.messages.chatdetail.ChatDetailViewModel
import com.mefy.platemate.presentation.features.main.messages.conversation.ConversationRoute
import com.mefy.platemate.presentation.features.main.messages.conversation.ConversationViewModel

internal fun NavGraphBuilder.messagesGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation<MessagesGraphDestination>(startDestination = MessagesDestination) {
        composable<MessagesDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            MessagesRoute(
                viewModel = hiltViewModel<MessagesViewModel>(parentEntry),
                onNavigateToChat = { conversationId, participantName, initials, avatarBgArgb, avatarFgArgb ->
                    navController.navigate(
                        ChatDestination(
                            conversationId = conversationId,
                            participantName = participantName,
                            initials = initials,
                            avatarBgArgb = avatarBgArgb,
                            avatarFgArgb = avatarFgArgb
                        )
                    )
                },
                modifier = modifier
            )
        }

        composable<ChatDestination> {
            ConversationRoute(
                viewModel = hiltViewModel<ConversationViewModel>(),
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChatDetail = { conversationId, participantName, initials, avatarBgArgb, avatarFgArgb ->
                    navController.navigate(
                        ChatDetailDestination(
                            conversationId = conversationId,
                            participantName = participantName,
                            initials = initials,
                            avatarBgArgb = avatarBgArgb,
                            avatarFgArgb = avatarFgArgb
                        )
                    )
                },
                modifier = modifier
            )
        }

        composable<ChatDetailDestination> {
            ChatDetailRoute(
                viewModel = hiltViewModel<ChatDetailViewModel>(),
                onNavigateBack = { navController.navigateUp() },
                modifier = modifier
            )
        }
    }
}
