package com.mefy.platemate.presentation.features.main.platedetail.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PlateActionsScreen(
    modifier: Modifier = Modifier,
    state: PlateActionsUiState,
    onAction: (PlateActionsUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors


    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16),
    ) {
        item {
            PMCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    PMPlateBadge(plate = state.plateCode)
                }
            }
        }

        item {
            PMRowItem(
                title = stringResource(
                    if (state.isSaved) R.string.platedetail_action_unsave else R.string.platedetail_action_save
                ),
                leadingIcon = if (state.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                showChevron = false,
                onClick = { onAction(PlateActionsUiAction.SaveToggled) }
            )
        }

        item {
            PMRowItem(
                title = stringResource(
                    if (state.isAlarmed) R.string.platedetail_action_remove_alarm else R.string.platedetail_action_alarm
                ),
                leadingIcon = if (state.isAlarmed) Icons.Filled.NotificationsActive else Icons.Outlined.AddAlert,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                showChevron = false,
                onClick = { onAction(PlateActionsUiAction.AlarmToggled) }
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.platedetail_action_report),
                leadingIcon = Icons.Outlined.Flag,
                leadingIconTint = colors.error,
                leadingContainerColor = colors.surfaceVariant,
                onClick = { onAction(PlateActionsUiAction.ReportClicked) }
            )
        }
    }
}

@Preview(name = "PlateActions Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateActionsLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateActionsScreen(state = previewState(), onAction = {})
    }
}

@Preview(name = "PlateActions Saved+Alarmed", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateActionsSavedPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateActionsScreen(
            state = previewState().copy(isSaved = true, isAlarmed = true),
            onAction = {}
        )
    }
}

@Preview(name = "PlateActions Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateActionsDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateActionsScreen(state = previewState(), onAction = {})
    }
}

private fun previewState() = PlateActionsUiState(
    isLoading = false,
    plateCode = "34 EK 0682",
    cityCode = "34",
    isSaved = false,
    isAlarmed = false
)
