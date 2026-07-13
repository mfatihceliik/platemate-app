package com.mefy.platemate.presentation.features.main.discover.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiState
import com.mefy.platemate.presentation.features.main.discover.DiscoverViewModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverFeedFilterUi
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

private val WINDOW_DAY_OPTIONS = listOf(
    7 to R.string.discover_filter_window_7,
    30 to R.string.discover_filter_window_30
)

@Composable
fun DiscoverFilterRoute(
    viewModel: DiscoverViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCityFilter: () -> Unit,
    onNavigateToReportTypeFilter: () -> Unit,
    onNavigateToPremiumInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Taslak, filtre butonuna basildiginda (DiscoverScreen) tohumlanir; burada tekrar tohumlanmaz,
    // aksi halde alt picker'lardan donuste (recomposition) kullanicinin secimi silinir.
    DiscoverFilterScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        onNavigateToCityFilter = onNavigateToCityFilter,
        onNavigateToReportTypeFilter = onNavigateToReportTypeFilter,
        onNavigateToPremiumInfo = onNavigateToPremiumInfo,
        modifier = modifier
    )
}

@Composable
fun DiscoverFilterScreen(
    state: DiscoverUiState,
    onAction: (DiscoverUiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCityFilter: () -> Unit,
    onNavigateToReportTypeFilter: () -> Unit,
    onNavigateToPremiumInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val draft = state.filterDraft

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.discover_filter_title),
            onBackClick = onNavigateBack
        ),
        bottomBar = {
            DiscoverFilterActions(
                onClear = {
                    onAction(DiscoverUiAction.FiltersCleared)
                    onNavigateBack()
                },
                onApply = {
                    onAction(DiscoverUiAction.FiltersApplied)
                    onNavigateBack()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dims.spacing.s16)
                .padding(
                    top = dims.spacing.s16,
                    bottom = dims.spacing.s16 + innerPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            // Sehir — coklu secim; detay ekranina gider.
            PMRowItem(
                title = stringResource(R.string.discover_filter_city_label),
                leadingIcon = Icons.Filled.Place,
                trailingText = citySummary(draft),
                onClick = onNavigateToCityFilter
            )

            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                PMSectionLabel(text = stringResource(R.string.discover_filter_min_rating_label))
                val currentRating = draft.minRating ?: 0
                PMRatingStars(
                    rating = currentRating,
                    interactive = true,
                    starSize = dims.sizing.iconLg,
                    onRatingChange = { newRating ->
                        // Ayni yildiza tekrar dokunmak secimi kaldirir.
                        onAction(DiscoverUiAction.DraftMinRatingChanged(if (newRating == currentRating) 0 else newRating))
                    }
                )
            }

            if (state.isPremium) {
                PMRowItem(
                    title = stringResource(R.string.discover_filter_report_type_label),
                    leadingIcon = Icons.Filled.Warning,
                    trailingText = draft.reportTypeLabel
                        ?: stringResource(R.string.discover_filter_report_type_any),
                    onClick = onNavigateToReportTypeFilter
                )

                Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                    PMSectionLabel(text = stringResource(R.string.discover_filter_window_label))
                    Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                        WINDOW_DAY_OPTIONS.forEach { (days, labelRes) ->
                            PMChip(
                                label = stringResource(labelRes),
                                selected = draft.windowDays == days,
                                onClick = {
                                    onAction(
                                        DiscoverUiAction.DraftWindowChanged(
                                            if (draft.windowDays == days) null else days
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            } else {
                PMRowItem(
                    title = stringResource(R.string.discover_filter_premium_locked),
                    leadingIcon = Icons.Filled.Lock,
                    leadingIconTint = MaterialTheme.pmColors.primary,
                    onClick = onNavigateToPremiumInfo
                )
            }
        }
    }
}

@Composable
private fun citySummary(draft: DiscoverFeedFilterUi): String = when {
    draft.cityIds.isEmpty() -> stringResource(R.string.discover_filter_city_all)
    draft.cityIds.size == 1 ->
        draft.cityNames.firstOrNull() ?: stringResource(R.string.discover_filter_city_count, 1)
    else -> stringResource(R.string.discover_filter_city_count, draft.cityIds.size)
}

@Composable
private fun DiscoverFilterActions(
    onClear: () -> Unit,
    onApply: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .navigationBarsPadding()
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        PMButton(
            text = stringResource(R.string.discover_filter_clear),
            onClick = onClear,
            variant = PMButtonVariant.Outlined,
            modifier = Modifier.weight(1f)
        )
        PMButton(
            text = stringResource(R.string.discover_filter_apply),
            onClick = onApply,
            variant = PMButtonVariant.Filled,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(name = "DiscoverFilter Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverFilterPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverFilterScreen(
            state = DiscoverUiState(
                isPremium = true,
                filterDraft = DiscoverFeedFilterUi(
                    cityIds = listOf(34, 6),
                    cityNames = listOf("İstanbul", "Ankara"),
                    minRating = 3
                )
            ),
            onAction = {},
            onNavigateBack = {},
            onNavigateToCityFilter = {},
            onNavigateToReportTypeFilter = {},
            onNavigateToPremiumInfo = {}
        )
    }
}
