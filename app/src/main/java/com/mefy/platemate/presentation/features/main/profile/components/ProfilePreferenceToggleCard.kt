package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun ProfilePreferenceToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val spacing = MaterialTheme.pmDimensions.spacing

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = spacing.s12, vertical = spacing.s12)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = title,
                    style = PMTextStyle.Label,
                    color = colors.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = spacing.s8)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )
            }
            PMText(
                text = description,
                style = PMTextStyle.Body,
                color = colors.onSurfaceVariant
            )
        }
    }
}

