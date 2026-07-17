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
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.features.main.platedetail.removal.components.ReasonRow
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
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMText(
                text = state.plateCode,
                style = PMTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            PMSectionLabel(text = stringResource(R.string.removal_request_reason_label))
            if (state.isLoadingReasons) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = colors.primary
                )
            } else {
                state.reasons.forEach { reason ->
                    ReasonRow(
                        label = reason.label,
                        isSelected = reason == state.selectedReason,
                        onClick = { onAction(PlateRemovalRequestUiAction.ReasonSelected(reason)) }
                    )
                }
            }

            if (state.selectedReason?.requiresDescription == true) {
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
}


