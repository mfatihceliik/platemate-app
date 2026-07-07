package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.DiscoverFilterUi
import com.mefy.platemate.presentation.features.main.discover.toLabelResId
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun DiscoverFilterChips(
    selectedFilter: DiscoverFilterUi,
    onSelected: (DiscoverFilterUi) -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        items(
            DiscoverFilterUi.entries,
            key = { it.name },
            contentType = { "filter_chip" }
        ) { filter ->
            val isSelected = selectedFilter == filter
            val bg = if (isSelected) colors.primary else colors.surfaceVariant
            val fg = if (isSelected) colors.onPrimary else colors.textSecondary

            Box(
                modifier = Modifier
                    .height(dims.sizing.chipHeight)
                    .clip(RoundedCornerShape(dims.radius.r12))
                    .background(bg)
                    .debouncedClickable { onSelected(filter) }
                    .padding(horizontal = dims.spacing.s16),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = stringResource(filter.toLabelResId()),
                    fontSize = dims.fontSize.md,
                    fontWeight = FontWeight.SemiBold,
                    color = fg
                )
            }
        }
    }
}