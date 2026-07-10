package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.theme.pmColors
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserProfileRoute(
    viewModel: UserProfileViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChat: (
        conversationId: String,
        otherUserId: Long,
        participantName: String,
    ) -> Unit, modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                UserProfileUiEffect.NavigateBack -> onNavigateBack()
                is UserProfileUiEffect.NavigateToChat -> onNavigateToChat(
                    effect.conversationId,
                    effect.otherUserId,
                    effect.participantName,
                )
            }
        }
    }

    val onAction = viewModel::onAction
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier, topBarConfig = PMTopBarConfig.Standard(
        title = stringResource(R.string.user_profile_title),
        onBackClick = { onAction(UserProfileUiAction.BackClicked) },
        actions = {
            IconButton(onClick = { onAction(UserProfileUiAction.ReportMenuClicked) }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.user_profile_more_options),
                    tint = colors.textPrimary
                )
            }
        }),
        loading = { innerPadding ->
        PMCircularProgressIndicator(
            fillMaxSize = true, modifier = Modifier.padding(innerPadding)
        )
    }) { innerPadding ->
        UserProfileScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
