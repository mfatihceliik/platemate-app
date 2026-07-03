package com.mefy.platemate.presentation.features.main.platedetail.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMRowPosition
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PlateActionsScreen(
    state: PlateActionsUiState,
    onAction: (PlateActionsUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.platedetail_actions_title),
            onBackClick = { onAction(PlateActionsUiAction.BackClicked) }
        ),
        status = if (state.isLoading) ScreenStatus.Loading else ScreenStatus.Content,
        loading = { innerPadding -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding)) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(cityCode = state.cityCode, size = PlateBadgeSize.Medium)
                PMText(text = state.plateCode, style = PMTextStyle.Title, color = colors.textPrimary)
            }

            // Flush group: rows sit gap-less so their border-overlap reads as one card.
            Column {
                PMRowItem(
                    title = stringResource(
                        if (state.isSaved) R.string.platedetail_action_unsave else R.string.platedetail_action_save
                    ),
                    leadingIcon = if (state.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    leadingIconTint = colors.primary,
                    leadingContainerColor = colors.primaryContainer,
                    position = PMRowPosition.Top,
                    showChevron = false,
                    onClick = { onAction(PlateActionsUiAction.SaveToggled) }
                )
                PMRowItem(
                    title = stringResource(
                        if (state.isAlarmed) R.string.platedetail_action_remove_alarm else R.string.platedetail_action_alarm
                    ),
                    leadingIcon = if (state.isAlarmed) Icons.Filled.NotificationsActive else Icons.Outlined.AddAlert,
                    leadingIconTint = colors.primary,
                    leadingContainerColor = colors.primaryContainer,
                    position = PMRowPosition.Middle,
                    showChevron = false,
                    onClick = { onAction(PlateActionsUiAction.AlarmToggled) }
                )
                PMRowItem(
                    title = stringResource(R.string.platedetail_action_report),
                    leadingIcon = Icons.Outlined.Flag,
                    leadingIconTint = colors.error,
                    leadingContainerColor = colors.surfaceVariant,
                    position = PMRowPosition.Bottom,
                    onClick = { onAction(PlateActionsUiAction.ReportClicked) }
                )
            }
        }
    }
}
