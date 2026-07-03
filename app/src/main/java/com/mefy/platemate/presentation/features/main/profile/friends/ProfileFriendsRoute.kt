package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProfileFriendsRoute(
    viewModel: ProfileFriendsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    val colors = MaterialTheme.pmColors
    val spacing = MaterialTheme.pmDimensions.spacing

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.friends.isEmpty() -> ScreenStatus.Empty
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_friends_title),
            onBackClick = onBackClick
        ),
        status = status,
        onRetry = viewModel::retry,
        loading = { innerPadding -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding)) },
        empty = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(spacing.s24),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = stringResource(R.string.profile_friends_empty),
                    style = PMTextStyle.Body,
                    color = colors.onSurfaceVariant
                )
            }
        }
    ) { innerPadding ->
        ProfileFriendsScreen(
            modifier = modifier,
            state = state,
            innerPadding = innerPadding
        )
    }
}
