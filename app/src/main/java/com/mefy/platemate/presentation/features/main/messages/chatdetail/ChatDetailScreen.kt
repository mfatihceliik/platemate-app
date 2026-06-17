package com.mefy.platemate.presentation.features.main.messages.chatdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMTopBarConfig
import com.mefy.platemate.presentation.features.main.messages.chatdetail.components.ChatDetailSettingsCard
import com.mefy.platemate.presentation.features.main.messages.chatdetail.components.ChatDetailUserCard
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ChatDetailScreen(
    state: ChatDetailUiState,
    onAction: (ChatDetailUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Simple(
            title = stringResource(R.string.chatdetail_title),
            onBackClick = { onAction(ChatDetailUiAction.BackClicked) }
        ),
        containerColor = MaterialTheme.pmColors.surfaceSecondary
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            item(key = "user_card") {
                ChatDetailUserCard(
                    participantName = state.participantName,
                    initials = state.initials,
                    avatarBg = state.avatarBg,
                    avatarFg = state.avatarFg,
                    onMessageClick = { onAction(ChatDetailUiAction.MessageClicked) }
                )
            }

            item(key = "settings_card") {
                ChatDetailSettingsCard(
                    notificationsEnabled = state.notificationsEnabled,
                    onNotificationsToggled = { onAction(ChatDetailUiAction.NotificationsToggled) },
                    onMediaClick = {},
                    onDeleteClick = { onAction(ChatDetailUiAction.DeleteChatClicked) },
                    onReportClick = { onAction(ChatDetailUiAction.ReportClicked) }
                )
            }
        }
    }
}
