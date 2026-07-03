package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCategoryCard
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverCategoryUi
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
internal fun DiscoverCategoryGrid(
    onCategoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val categories = remember(colors) {
        listOf(
            DiscoverCategoryUi(
                titleResId = R.string.discover_category_kindest,
                countResId = R.string.discover_category_kindest_count,
                backgroundColor = colors.categoryTealBg,
                foregroundColor = colors.categoryTealFg,
                iconColor = colors.categoryTealIcon
            ),
            DiscoverCategoryUi(
                titleResId = R.string.discover_category_quickrespond,
                countResId = R.string.discover_category_quickrespond_count,
                backgroundColor = colors.categoryIndigoBg,
                foregroundColor = colors.categoryIndigoFg,
                iconColor = colors.categoryIndigoIcon
            ),
            DiscoverCategoryUi(
                titleResId = R.string.discover_category_careful,
                countResId = R.string.discover_category_careful_count,
                backgroundColor = colors.categoryOrangeBg,
                foregroundColor = colors.categoryOrangeFg,
                iconColor = colors.categoryOrangeIcon
            ),
            DiscoverCategoryUi(
                titleResId = R.string.discover_category_helpful,
                countResId = R.string.discover_category_helpful_count,
                backgroundColor = colors.categoryGreenBg,
                foregroundColor = colors.categoryGreenFg,
                iconColor = colors.categoryGreenIcon
            )
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        categories.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                rowItems.forEach { category ->
                    PMCategoryCard(
                        title = stringResource(category.titleResId),
                        count = stringResource(category.countResId),
                        backgroundColor = category.backgroundColor,
                        foregroundColor = category.foregroundColor,
                        iconColor = category.iconColor,
                        onClick = onCategoryClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Keep card width stable when a row is not full.
                repeat(2 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}