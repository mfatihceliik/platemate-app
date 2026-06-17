package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserProfileRoute(
    viewModel: UserProfileViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChat: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                UserProfileUiEffect.NavigateBack -> onNavigateBack()
                is UserProfileUiEffect.NavigateToChat -> onNavigateToChat(effect.userId)
            }
        }
    }

    UserProfileScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
