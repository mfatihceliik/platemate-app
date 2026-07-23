package com.mefy.platemate.presentation.features.main.discover.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction
import com.mefy.platemate.presentation.features.main.discover.DiscoverViewModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

private val RATING_OPTIONS = listOf(5, 4, 3, 2, 1)

@Composable
fun DiscoverRatingFilterRoute(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverRatingFilterScreen(
        selectedRating = state.filterDraft.minRating,
        onSelect = { rating ->
            // 0 = Farketmez; VM takeIf { it > 0 } ile temizler.
            viewModel.onAction(DiscoverUiAction.DraftMinRatingChanged(rating))
            navController.navigateUp()
        },
        onNavigateBack = { navController.navigateUp() },
        modifier = modifier
    )
}

@Composable
fun DiscoverRatingFilterScreen(
    modifier: Modifier = Modifier,
    selectedRating: Int?,
    onSelect: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.discover_filter_min_rating_label),
            onBackClick = onNavigateBack
        ),
        contentPadding = PaddingValues(bottom = spacing.s16)
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            contentPadding = pad
        ) {
            item(key = "rating_any") {
                RatingOptionRow(
                    rating = null,
                    selected = selectedRating == null || selectedRating == 0,
                    onClick = { onSelect(0) }
                )
            }
            items(items = RATING_OPTIONS, key = { it }) { rating ->
                RatingOptionRow(
                    rating = rating,
                    selected = selectedRating == rating,
                    onClick = { onSelect(rating) }
                )
            }
        }
    }
}

@Composable
private fun RatingOptionRow(
    rating: Int?,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .debouncedClickable(onClick = onClick)
            .padding(horizontal = spacing.s16, vertical = spacing.s12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            if (rating == null) {
                PMText(
                    text = stringResource(R.string.discover_filter_report_type_any),
                    style = PMTextStyle.Body,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (selected) colors.primary else colors.textPrimary
                )
            } else {
                PMRatingStars(rating = rating, starSize = sizing.iconMd)
                PMText(
                    text = stringResource(R.string.discover_filter_rating_upwards),
                    style = PMTextStyle.Body,
                    color = if (selected) colors.primary else colors.textSecondary
                )
            }
        }
        if (selected) {
            PMIcon(
                imageVector = Icons.Filled.Check,
                size = sizing.iconSm,
                tint = colors.primary
            )
        }
    }
}

@Preview(name = "DiscoverRatingFilter", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverRatingFilterPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverRatingFilterScreen(
            selectedRating = 4,
            onSelect = {},
            onNavigateBack = {}
        )
    }
}
