package com.mefy.platemate.presentation.features.main.discover.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction
import com.mefy.platemate.presentation.features.main.discover.DiscoverViewModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeOptionUi
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun DiscoverReportTypeFilterRoute(
    viewModel: DiscoverViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverReportTypeFilterScreen(
        options = state.availableReportTypes,
        selectedCode = state.filterDraft.reportTypeCode,
        onSelect = { code, label ->
            viewModel.onAction(DiscoverUiAction.DraftReportTypeChanged(code, label))
            // Tek secim; secimden hemen sonra filtre ekranina don.
            onNavigateBack()
        },
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun DiscoverReportTypeFilterScreen(
    options: List<DiscoverReportTypeOptionUi>,
    selectedCode: String?,
    onSelect: (code: String?, label: String?) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.discover_filter_report_type_title),
            onBackClick = onNavigateBack
        ),
        contentPadding = PaddingValues(bottom = dims.spacing.s16)
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            contentPadding = pad
        ) {
            item(key = "report_type_any") {
                ReportTypeOptionRow(
                    label = stringResource(R.string.discover_filter_report_type_any),
                    selected = selectedCode == null,
                    onClick = { onSelect(null, null) }
                )
            }
            items(items = options, key = { it.code }) { option ->
                ReportTypeOptionRow(
                    label = option.label,
                    selected = selectedCode == option.code,
                    onClick = { onSelect(option.code, option.label) }
                )
            }
        }
    }
}

@Composable
private fun ReportTypeOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .debouncedClickable(onClick = onClick)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMText(
            modifier = Modifier.weight(1f),
            text = label,
            style = PMTextStyle.Body,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) colors.primary else colors.textPrimary
        )
        if (selected) {
            PMIcon(
                imageVector = Icons.Filled.Check,
                size = dims.sizing.iconSm,
                tint = colors.primary
            )
        }
    }
}

@Preview(name = "DiscoverReportTypeFilter", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverReportTypeFilterPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverReportTypeFilterScreen(
            options = listOf(
                DiscoverReportTypeOptionUi(code = "DANGEROUS_DRIVING", label = "Tehlikeli Sürüş", colorHex = "#E53935"),
                DiscoverReportTypeOptionUi(code = "PARKING", label = "Hatalı Park", colorHex = "#FFB300")
            ),
            selectedCode = "PARKING",
            onSelect = { _, _ -> },
            onNavigateBack = {}
        )
    }
}
