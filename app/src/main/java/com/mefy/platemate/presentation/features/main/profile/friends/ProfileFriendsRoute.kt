package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileFriendsRoute(
    viewModel: ProfileFriendsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProfileFriendsScreen(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
