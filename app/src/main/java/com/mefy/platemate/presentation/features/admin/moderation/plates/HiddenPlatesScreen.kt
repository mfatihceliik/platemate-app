package com.mefy.platemate.presentation.features.admin.moderation.plates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.moderation.plates.components.HiddenPlateCard
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun HiddenPlatesScreen(
    state: HiddenPlatesUiState,
    onAction: (HiddenPlatesUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (state.isEmpty) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_hidden_plates_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            itemsIndexed(items = state.items, key = { _, item -> item.id }) { index, item ->
                if (index >= state.items.lastIndex) {
                    LaunchedEffect(state.items.size) { onAction(HiddenPlatesUiAction.LoadMore) }
                }
                HiddenPlateCard(
                    model = item,
                    isActioning = state.actioningId == item.id,
                    onRestore = { onAction(HiddenPlatesUiAction.RestoreClicked(item.id)) }
                )
            }
            if (state.isLoadingMore) {
                item {
                    Box(Modifier.fillMaxWidth().padding(dims.spacing.s12), contentAlignment = Alignment.Center) {
                        PMCircularProgressIndicator()
                    }
                }
            }
        }
    }
}

private val hiddenPlatesPreviewState = HiddenPlatesUiState(
    isLoading = false,
    items = listOf(
        HiddenPlateUiModel(
            id = 1L,
            plateCode = "34 EK 0682",
            statusCode = "HIDDEN",
            hiddenReason = "Çok sayıda şikayet aldı.",
            reportCount = 7
        ),
        HiddenPlateUiModel(
            id = 2L,
            plateCode = "06 ABC 123",
            statusCode = "HIDDEN",
            hiddenReason = "",
            reportCount = 4
        )
    )
)

@Preview(name = "HiddenPlates Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HiddenPlatesScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        HiddenPlatesScreen(state = hiddenPlatesPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "HiddenPlates Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun HiddenPlatesScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        HiddenPlatesScreen(state = hiddenPlatesPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "HiddenPlates Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HiddenPlatesScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        HiddenPlatesScreen(state = HiddenPlatesUiState(isLoading = false), onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}
