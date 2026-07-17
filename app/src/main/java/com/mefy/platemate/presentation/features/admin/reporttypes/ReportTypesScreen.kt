package com.mefy.platemate.presentation.features.admin.reporttypes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.reporttypes.components.ReportTypeRow
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ReportTypesScreen(
    state: ReportTypesUiState,
    onAction: (ReportTypesUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_report_types_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            items(items = state.items, key = { it.id }) { item ->
                ReportTypeRow(
                    item = item,
                    onClick = { onAction(ReportTypesUiAction.EditClicked(item.id)) },
                    onToggle = { onAction(ReportTypesUiAction.ActiveToggled(item.id, item.active)) }
                )
            }
        }
    }
}

private val reportTypesPreviewState = ReportTypesUiState(
    isLoading = false,
    items = listOf(
        ReportTypeListItem(id = 1L, code = "SPAM", label = "Spam", severityCode = "YELLOW", active = true),
        ReportTypeListItem(id = 2L, code = "ABUSE", label = "Taciz", severityCode = "RED", active = false)
    )
)

@Preview(name = "ReportTypes Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReportTypesScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportTypesScreen(state = reportTypesPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "ReportTypes Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReportTypesScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReportTypesScreen(state = reportTypesPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "ReportTypes Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReportTypesScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportTypesScreen(state = ReportTypesUiState(isLoading = false), onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}
