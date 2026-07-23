package com.mefy.platemate.presentation.features.main.platedetail.removal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.plate.PlateRemovalReason
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMCommentField
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMRadioButton
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.components.pmRowPositionOf
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateRemovalRequestScreen(
    modifier: Modifier = Modifier,
    state: PlateRemovalRequestUiState,
    onAction: (PlateRemovalRequestUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    val onSubmitClicked = remember(onAction) { { onAction(PlateRemovalRequestUiAction.SubmitClicked) } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = spacedByWithFooter()
    ) {
        item {
            PMPlateBadge(plate = state.plateCode)
        }

        item {
            PMSectionLabel(text = stringResource(R.string.removal_request_reason_label))
        }

        if (state.isLoadingReasons) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
        } else {
            itemsIndexed(items = state.reasons, key = { _, reason -> reason.id }) { index, reason ->
                PMRowItem(
                    title = reason.label,
                    position = pmRowPositionOf(index, state.reasons.size),
                    showChevron = false,
                    trailing = {
                        PMRadioButton(
                            selected = reason == state.selectedReason,
                            onClick = null
                        )
                    },
                    onClick = { onAction(PlateRemovalRequestUiAction.ReasonSelected(reason)) }
                )
            }
        }

        if (state.selectedReason?.requiresDescription == true) {
            item {
                Column {
                    PMSectionLabel(text = stringResource(R.string.removal_request_description_hint))
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

        item {
            PMButton(
                text = stringResource(R.string.removal_request_submit),
                onClick = onSubmitClicked,
                enabled = state.isSubmitEnabled,
                loading = state.isSubmitting,
                modifier = Modifier.fillMaxWidth().padding(vertical = spacing.s24)
            )
        }
    }
}

private val plateRemovalRequestPreviewReasons = listOf(
    PlateRemovalReason(id = 1L, code = "SOLD", label = "Aracı sattım", requiresDescription = false),
    PlateRemovalReason(id = 2L, code = "MISMATCH", label = "Plaka bilgisi hatalı", requiresDescription = true),
    PlateRemovalReason(id = 3L, code = "OTHER", label = "Diğer", requiresDescription = true)
)

private val plateRemovalRequestPreviewState = PlateRemovalRequestUiState(
    plateCode = "34 AB 1234",
    reasons = plateRemovalRequestPreviewReasons,
    isLoadingReasons = false,
    selectedReason = plateRemovalRequestPreviewReasons[1],
    description = "Plaka bilgileri yanlış girilmiş, düzeltilmesini istiyorum."
)

@Preview(name = "PlateRemovalRequest Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateRemovalRequestScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalRequestScreen(state = plateRemovalRequestPreviewState, onAction = {})
    }
}

@Preview(name = "PlateRemovalRequest Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateRemovalRequestScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateRemovalRequestScreen(state = plateRemovalRequestPreviewState, onAction = {})
    }
}
