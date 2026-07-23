package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileShimmerContent
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
fun UserProfileRoute(
    viewModel: UserProfileViewModel,
    onNavigateToChat: (
        conversationId: String,
        otherUserId: Long,
        participantName: String,
    ) -> Unit,
    onNavigateToFollowList: (userId: String, initialTab: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                UserProfileUiEffect.NavigateBack -> navController.navigateUp()
                is UserProfileUiEffect.NavigateToChat -> onNavigateToChat(
                    effect.conversationId,
                    effect.otherUserId,
                    effect.participantName,
                )
                is UserProfileUiEffect.NavigateToFollowList -> onNavigateToFollowList(
                    effect.userId,
                    effect.initialTab,
                )
            }
        }
    }

    val onAction = viewModel::onAction
    val onBackClicked = remember(onAction) { { onAction(UserProfileUiAction.BackClicked)} }
    val sizing = PMTheme.sizing
    val colors = PMTheme.colors

    val status = if (state.isLoading) ScreenStatus.Loading else ScreenStatus.Content

    PMBaseScreen(
        modifier = modifier,
        status = status,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.user_profile_title),
            alignment = PMTopBarAlignment.Start,
            onBackClick = onBackClicked,
            actions = {
                PMIconButton(
                    imageVector = Icons.Filled.MoreVert,
                    iconColor = colors.iconDefault,
                    size = sizing.iconMd,
                    onClick = { onAction(UserProfileUiAction.ReportMenuClicked) }
                )
            }
        ),
        loading = { innerPadding ->
            UserProfileShimmerContent(
                modifier = Modifier.padding(innerPadding)
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
