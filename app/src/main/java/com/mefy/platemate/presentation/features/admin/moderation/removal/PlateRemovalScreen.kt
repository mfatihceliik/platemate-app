package com.mefy.platemate.presentation.features.admin.moderation.removal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateRemovalScreen(
    modifier: Modifier = Modifier,
    state: PlateRemovalUiState,
    onAction: (PlateRemovalUiAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    if (state.isEmpty) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_removal_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            itemsIndexed(items = state.items, key = { _, item -> item.id }) { index, item ->
                if (index >= state.items.lastIndex) {
                    LaunchedEffect(state.items.size) { onAction(PlateRemovalUiAction.LoadMore) }
                }
                RemovalCard(
                    model = item,
                    isActioning = state.actioningId == item.id,
                    onAccept = { onAction(PlateRemovalUiAction.AcceptClicked(item.id)) },
                    onReject = { onAction(PlateRemovalUiAction.RejectClicked(item.id)) }
                )
            }
            if (state.isLoadingMore) {
                item {
                    Box(Modifier.fillMaxWidth().padding(spacing.s12), contentAlignment = Alignment.Center) {
                        PMCircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun RemovalCard(
    model: PlateRemovalUiModel,
    isActioning: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            PMText(text = model.plateCode, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = model.reasonCode, style = PMTextStyle.Note, color = colors.primary)
        }
        val requester = listOf(model.requesterUsername, model.requesterEmail)
            .filter { it.isNotBlank() }
            .joinToString(" · ")
        if (requester.isNotBlank()) {
            PMText(text = requester, style = PMTextStyle.Note, color = colors.textLabel)
        }
        if (model.description.isNotBlank()) {
            PMText(text = model.description, style = PMTextStyle.Body, color = colors.textPrimary)
        }
        PMText(text = model.date, style = PMTextStyle.Note, color = colors.textLabel)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
            PMButton(
                text = stringResource(R.string.admin_request_reject),
                onClick = onReject,
                variant = PMButtonVariant.Outlined,
                enabled = !isActioning,
                modifier = Modifier.weight(1f)
            )
            PMButton(
                text = stringResource(R.string.admin_request_accept),
                onClick = onAccept,
                enabled = !isActioning,
                loading = isActioning,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private val plateRemovalPreviewModel = PlateRemovalUiModel(
    id = 1L,
    plateCode = "34 EK 0682",
    requesterUsername = "ahmety",
    requesterEmail = "ahmet@example.com",
    reasonCode = "PRIVACY",
    description = "Plaka bilgilerimin kaldırılmasını istiyorum.",
    date = "2026-06-24"
)

private val plateRemovalPreviewState = PlateRemovalUiState(
    isLoading = false,
    items = listOf(plateRemovalPreviewModel)
)

@Preview(name = "PlateRemoval Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateRemovalScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalScreen(
            state = plateRemovalPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PlateRemoval Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateRemovalScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateRemovalScreen(
            state = plateRemovalPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PlateRemoval Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateRemovalScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalScreen(
            state = PlateRemovalUiState(
                isLoading = false),
            onAction = {}
        )
    }
}

@Preview(name = "RemovalCard", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun RemovalCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        RemovalCard(
            model = plateRemovalPreviewModel,
            isActioning = false,
            onAccept = {},
            onReject = {}
        )
    }
}
