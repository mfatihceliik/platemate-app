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
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileShimmerContent
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun UserProfileRoute(
    viewModel: UserProfileViewModel, onNavigateBack: () -> Unit, onNavigateToChat: (
        conversationId: String,
        otherUserId: Long,
        participantName: String,
    ) -> Unit, modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
    val onBackClicked = remember(onAction) { { onAction(UserProfileUiAction.BackClicked)} }
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.user_profile_title),
            alignment = PMTopBarAlignment.Start,
            onBackClick = onBackClicked,
            actions = {
                PMIconButton(
                    imageVector = Icons.Filled.MoreVert,
                    iconColor = colors.iconDefault,
                    size = dims.sizing.iconMd,
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
