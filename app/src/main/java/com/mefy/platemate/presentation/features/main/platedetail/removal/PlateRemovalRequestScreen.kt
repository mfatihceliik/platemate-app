package com.mefy.platemate.presentation.features.main.platedetail.removal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMCommentField
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.platedetail.removal.model.PlateRemovalReason
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PlateRemovalRequestScreen(
    state: PlateRemovalRequestUiState,
    onAction: (PlateRemovalRequestUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.removal_request_title),
            onBackClick = { onAction(PlateRemovalRequestUiAction.BackClicked) }
        ),
        bottomBar = {
            Box(Modifier.fillMaxWidth().background(colors.surface).padding(dims.spacing.s16)) {
                PMButton(
                    text = stringResource(R.string.removal_request_submit),
                    onClick = { onAction(PlateRemovalRequestUiAction.SubmitClicked) },
                    enabled = state.isSubmitEnabled,
                    loading = state.isSubmitting,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMText(
                text = state.plateCode,
                style = PMTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            PMSectionLabel(text = stringResource(R.string.removal_request_reason_label))
            PlateRemovalReason.entries.forEach { reason ->
                ReasonRow(
                    label = stringResource(reason.labelRes),
                    isSelected = reason == state.selectedReason,
                    onClick = { onAction(PlateRemovalRequestUiAction.ReasonSelected(reason)) }
                )
            }

            PMSectionLabel(
                text = stringResource(R.string.removal_request_description_hint),
                modifier = Modifier.padding(top = dims.spacing.s8)
            )
            PMCommentField(
                value = state.description,
                onValueChange = { onAction(PlateRemovalRequestUiAction.DescriptionChanged(it)) },
                maxLength = PlateRemovalRequestUiState.DESCRIPTION_MAX_LENGTH,
                placeholder = stringResource(R.string.removal_request_description_hint),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ReasonRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shape = MaterialTheme.shapes.small

    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surfaceSecondary)
            .border(
                width = dims.stroke.st2,
                color = if (isSelected) colors.primary else colors.cardBorder,
                shape = shape
            )
            .debouncedClickable(onClick = onClick)
            .padding(dims.spacing.s12),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dims.spacing.s24)
                .clip(CircleShape)
                .border(dims.stroke.st2, if (isSelected) colors.primary else colors.disabled, CircleShape)
                .background(if (isSelected) colors.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(dims.spacing.s8)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
        PMText(text = label, style = PMTextStyle.Body, color = colors.textPrimary, modifier = Modifier.fillMaxWidth())
    }
}
