package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMTagChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shape = RoundedCornerShape(dims.radius.rFull)

    val bg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val fg = if (isSelected) MaterialTheme.colorScheme.onPrimary else colors.textSecondary
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else colors.chipBg

    Box(
        modifier = modifier
            .height(dims.sizing.chipHeight)
            .clip(shape)
            .background(bg)
            .border(dims.stroke.st1, borderColor, shape)
            .debouncedClickable(onClick = onClick)
            .padding(horizontal = dims.spacing.s16),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(name = "PMTagChip", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMTagChipPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val tags = listOf("Nazik", "Yol verdi", "Saygili", "Tesekkur etti", "Sabirli", "Dikkatli")
        var selected by remember { mutableStateOf(setOf(0, 1, 2)) }

        FlowRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEachIndexed { index, tag ->
                PMTagChip(
                    text = tag,
                    isSelected = index in selected,
                    onClick = {
                        selected = if (index in selected) selected - index else selected + index
                    }
                )
            }
        }
    }
}
