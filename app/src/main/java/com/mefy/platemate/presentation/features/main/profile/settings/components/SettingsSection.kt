package com.mefy.platemate.presentation.features.main.profile.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SettingsSection(
    label: String,
    content: @Composable () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
        PMText(
            text = label,
            style = PMTextStyle.SectionLabel,
            modifier = Modifier.padding(start = dims.spacing.s4)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
                .border(dims.stroke.st1, colors.cardBorder, MaterialTheme.shapes.large)
        ) {
            content()
        }
    }
}
