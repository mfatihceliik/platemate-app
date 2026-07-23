package com.mefy.platemate.presentation.features.admin.plateremovalreasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.plateremovalreasons.components.PlateRemovalReasonRow
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateRemovalReasonsScreen(
    modifier: Modifier = Modifier,
    state: PlateRemovalReasonsUiState,
    onAction: (PlateRemovalReasonsUiAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_plate_removal_reasons_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            items(items = state.items, key = { it.id }) { item ->
                PlateRemovalReasonRow(
                    item = item,
                    onClick = { onAction(PlateRemovalReasonsUiAction.EditClicked(item.id)) },
                    onToggle = { onAction(PlateRemovalReasonsUiAction.ActiveToggled(item.id, item.active)) }
                )
            }
        }
    }
}

private val PlateRemovalReasonsPreviewState = PlateRemovalReasonsUiState(
    isLoading = false,
    items = listOf(
        PlateRemovalReasonListItem(id = 6L, code = "SPAM", label = "Spam", requiresDescription = false, active = true),
        PlateRemovalReasonListItem(id = 7L, code = "OTHER", label = "Diğer", requiresDescription = true, active = true),
        PlateRemovalReasonListItem(id = 8L, code = "FAKE_PLATE", label = "Sahte plaka", requiresDescription = false, active = false)
    )
)

@Preview(name = "PlateRemovalReasons Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateRemovalReasonsScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalReasonsScreen(
            state = PlateRemovalReasonsPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PlateRemovalReasons Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateRemovalReasonsScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateRemovalReasonsScreen(
            state = PlateRemovalReasonsPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PlateRemovalReasons Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateRemovalReasonsScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalReasonsScreen(
            state = PlateRemovalReasonsUiState(isLoading = false),
            onAction = {}
        )
    }
}

