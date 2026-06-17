package com.mefy.platemate.presentation.features.main.profile.settings.language.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.profile.settings.language.LanguageItem
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun LanguageRow(
    item: LanguageItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    showDivider: Boolean
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (item.language != null) Modifier.debouncedClickable(onClick = onSelect)
                    else Modifier
                )
                .padding(dims.spacing.s12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
                PMText(
                    text = item.displayName,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = colors.textPrimary
                )
                PMText(
                    text = item.nativeName,
                    color = colors.textLabel
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dims.sizing.iconLg)
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}
