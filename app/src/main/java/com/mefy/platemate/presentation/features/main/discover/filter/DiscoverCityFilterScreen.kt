package com.mefy.platemate.presentation.features.main.discover.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction
import com.mefy.platemate.presentation.features.main.discover.DiscoverViewModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/** Filtrede en fazla secilebilecek sehir sayisi. */
private const val MAX_FILTER_CITIES = 5

@Composable
fun DiscoverCityFilterRoute(
    viewModel: DiscoverViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverCityFilterScreen(
        selectedCityIds = state.filterDraft.cityIds,
        onCitiesChanged = { ids, names ->
            viewModel.onAction(DiscoverUiAction.DraftCitiesChanged(ids, names))
        },
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun DiscoverCityFilterScreen(
    selectedCityIds: List<Int>,
    onCitiesChanged: (List<Int>, List<String>) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val allCities = remember { CityNameResolver.allCities() }
    var query by remember { mutableStateOf("") }

    val selected = selectedCityIds.toSet()
    // Bas harfe gore (prefix) filtre; "i" -> "i" ile baslayan sehirler ("iceren" degil).
    val filtered = remember(query) {
        if (query.isBlank()) allCities
        else allCities.filter { it.second.startsWith(query.trim(), ignoreCase = true) }
    }
    val atLimit = selected.size >= MAX_FILTER_CITIES

    // Secili sehir id'lerinden, plaka koduna gore sirali isimleri uretir.
    fun namesFor(ids: Set<Int>): List<String> =
        allCities.filter { it.first in ids }.map { it.second }

    fun toggle(cityId: Int) {
        // En fazla MAX_FILTER_CITIES sehir; limitteyken yeni ekleme yok, cikarma her zaman calisir.
        if (cityId !in selected && selected.size >= MAX_FILTER_CITIES) return
        val newIds = if (cityId in selected) selected - cityId else selected + cityId
        val orderedIds = allCities.map { it.first }.filter { it in newIds }
        onCitiesChanged(orderedIds, namesFor(newIds))
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.discover_filter_city_title),
            onBackClick = onNavigateBack
        ),
        contentPadding = PaddingValues(bottom = dims.spacing.s16)
    ) { pad ->
        Column(modifier = Modifier.fillMaxSize()) {
            PMSearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(R.string.discover_filter_city_search_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s8)
            )

            if (selected.isNotEmpty()) {
                SelectedCitiesHeader(
                    selectedNames = namesFor(selected),
                    atLimit = atLimit,
                    onRemove = { name ->
                        val id = allCities.firstOrNull { it.second == name }?.first
                        if (id != null) toggle(id)
                    },
                    onClearAll = { onCitiesChanged(emptyList(), emptyList()) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background),
                contentPadding = pad
            ) {
                items(items = filtered, key = { it.first }) { (cityId, cityName) ->
                    CityOptionRow(
                        label = cityName,
                        selected = cityId in selected,
                        onClick = { toggle(cityId) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedCitiesHeader(
    selectedNames: List<String>,
    atLimit: Boolean,
    onRemove: (String) -> Unit,
    onClearAll: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s8),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(
                text = stringResource(R.string.discover_filter_city_count, selectedNames.size),
                style = PMTextStyle.SectionLabel,
                color = colors.textSecondary
            )
            PMText(
                text = stringResource(R.string.discover_filter_city_clear),
                style = PMTextStyle.Label,
                color = colors.primary,
                modifier = Modifier.debouncedClickable(onClick = onClearAll)
            )
        }
        if (atLimit) {
            PMText(
                text = stringResource(R.string.discover_filter_city_limit, MAX_FILTER_CITIES),
                style = PMTextStyle.Caption,
                color = colors.textTertiary
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            selectedNames.forEach { name ->
                PMChip(
                    label = name,
                    selected = true,
                    onClick = { onRemove(name) }
                )
            }
        }
    }
}

@Composable
private fun CityOptionRow(
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

@Preview(name = "DiscoverCityFilter", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverCityFilterPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverCityFilterScreen(
            selectedCityIds = listOf(34, 6),
            onCitiesChanged = { _, _ -> },
            onNavigateBack = {}
        )
    }
}
