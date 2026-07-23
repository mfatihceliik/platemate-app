package com.mefy.platemate.presentation.features.main.messages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.features.main.messages.components.MessagesEmptyState
import com.mefy.platemate.presentation.features.main.messages.components.MessagesShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MessagesRoute(
    viewModel: MessagesViewModel,
    onNavigateToChat: (
        conversationId: String,
        participantName: String,
        messageId: Long?
        ) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* sonuç sistemce kalıcı; ek işlem gerekmez */ }

    LaunchedEffect(Unit) {
        val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        if (needsPermission && !viewModel.hasRequestedNotificationPermission()) {
            viewModel.markNotificationPermissionRequested()
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is MessagesUiEffect.NavigateToChat -> onNavigateToChat(
                    effect.conversationId,
                    effect.participantName,
                    effect.messageId,
                )
            }
        }
    }

    val onAction = viewModel::onAction

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.conversations.isEmpty() -> ScreenStatus.Empty
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.main_tab_messages),
            alignment = PMTopBarAlignment.Start,
            showBackButton = false
        ),
        viewModel = viewModel,
        status = status,
        loading = { innerPadding ->
            MessagesShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        },
        empty = { innerPadding ->
            MessagesEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }) { innerPadding ->
        MessagesScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = modifier,
            innerPadding = innerPadding
        )
    }
}
