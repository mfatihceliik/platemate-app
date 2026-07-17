package com.mefy.platemate.presentation.features.main.discover.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction
import com.mefy.platemate.presentation.features.main.discover.DiscoverViewModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

const val MAX_CUSTOM_WINDOW_DAYS = 365

private val WINDOW_PRESETS: List<Pair<Int?, Int>> = listOf(
    null to R.string.discover_filter_report_type_any,
    1 to R.string.discover_filter_window_preset_1d,
    7 to R.string.discover_filter_window_preset_1w,
    14 to R.string.discover_filter_window_preset_2w,
    30 to R.string.discover_filter_window_preset_1m,
    90 to R.string.discover_filter_window_preset_3m,
    180 to R.string.discover_filter_window_preset_6m,
    365 to R.string.discover_filter_window_preset_1y
)

private val PRESET_DAYS: Set<Int> = WINDOW_PRESETS.mapNotNull { it.first }.toSet()

@Composable
fun DiscoverWindowFilterRoute(
    viewModel: DiscoverViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverWindowFilterScreen(
        selectedDays = state.filterDraft.windowDays,
        onWindowChanged = { days ->
            viewModel.onAction(DiscoverUiAction.DraftWindowChanged(days))
        },
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun DiscoverWindowFilterScreen(
    selectedDays: Int?,
    onWindowChanged: (Int?) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    // Preset olmayan (ozel) bir deger varsa alan onunla baslar.
    var customText by remember {
        mutableStateOf(
            selectedDays?.takeIf { it !in PRESET_DAYS }?.toString().orEmpty()
        )
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.discover_filter_window_label),
            onBackClick = onNavigateBack
        ),
        contentPadding = PaddingValues(bottom = dims.spacing.s16)
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .verticalScroll(rememberScrollState())
                .padding(pad)
        ) {
            WINDOW_PRESETS.forEach { (days, labelRes) ->
                WindowOptionRow(
                    label = stringResource(labelRes),
                    selected = selectedDays == days,
                    onClick = {
                        customText = ""
                        onWindowChanged(days)
                    }
                )
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = dims.spacing.s16,
                    vertical = dims.spacing.s12
                ),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                PMSectionLabel(text = stringResource(R.string.discover_filter_window_custom_label))
                PMTextField(
                    value = customText,
                    onValueChange = { raw ->
                        // Yalnizca rakam, en fazla 3 hane.
                        val digits = raw.filter { it.isDigit() }.take(3)
                        customText = digits
                        val days = digits.toIntOrNull()
                        if (days != null && days in 1..MAX_CUSTOM_WINDOW_DAYS) {
                            onWindowChanged(days)
                        }
                    },
                    placeholder = stringResource(R.string.discover_filter_window_custom_hint),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun WindowOptionRow(
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

@Preview(name = "DiscoverWindowFilter", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverWindowFilterPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverWindowFilterScreen(
            selectedDays = 30,
            onWindowChanged = {},
            onNavigateBack = {}
        )
    }
}
