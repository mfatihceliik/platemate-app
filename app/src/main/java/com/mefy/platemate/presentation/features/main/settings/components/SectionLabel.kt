package com.mefy.platemate.presentation.features.main.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun SectionLabel(
    modifier: Modifier = Modifier,
    text: String
) {
    val dims = MaterialTheme.pmDimensions
    PMText(
        text = text,
        style = PMTextStyle.SectionLabel,
        modifier = modifier.padding(
            start = dims.spacing.s4,
            top = dims.spacing.s8,
            bottom = dims.spacing.s4
        )
    )
}