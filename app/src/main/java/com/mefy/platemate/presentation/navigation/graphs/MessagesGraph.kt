package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
        screenComposable<MessagesDestination, MessagesViewModel>(
            viewModel = { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainGraphDestination)
                }
                hiltViewModel<MessagesViewModel>(parentEntry)
            },
        ) { viewModel ->
            MessagesRoute(
                viewModel = viewModel,
                onNavigateToChat = { conversationId, participantName ->
                    navController.navigate(
                        ChatDestination(
                            conversationId = conversationId,
                            participantName = participantName,
                        )
                    )
                },
                modifier = modifier
            )
        }

        screenComposable<ChatDestination, ConversationViewModel>(
            viewModel = { hiltViewModel<ConversationViewModel>() },
        ) { viewModel ->
            ConversationRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChatDetail = { conversationId, participantName ->
                    navController.navigate(
                        ChatDetailDestination(
                            conversationId = conversationId,
                            participantName = participantName,
                        )
                    )
                },
                modifier = modifier
            )
        }

        screenComposable<ChatDetailDestination, ChatDetailViewModel>(
            viewModel = { hiltViewModel<ChatDetailViewModel>() },
        ) { viewModel ->
            ChatDetailRoute(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToUserProfile = { userId ->
                    navController.navigateToUserProfile(userId.toString())
                },
                onNavigateToMessagesList = {
                    navController.popBackStack(MessagesDestination, inclusive = false)
                },
                modifier = modifier
            )
        }
    }
}
