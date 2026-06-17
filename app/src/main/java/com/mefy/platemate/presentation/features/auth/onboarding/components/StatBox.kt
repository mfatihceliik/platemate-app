package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun StatBox(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    val pmColors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions

    Column(
        modifier = modifier
            .background(colorScheme.surface, RoundedCornerShape(dimensions.radius.r16))
            .padding(vertical = dimensions.spacing.s12, horizontal = dimensions.spacing.s8)
            .shadow(dimensions.spacing.s8, spotColor = pmColors.cardShadow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.s4)
    ) {
        PMText(text = value, style = PMTextStyle.Display, color = color)
        PMText(text = label, style = PMTextStyle.Caption, color = pmColors.textTertiary, textAlign = TextAlign.Center)
    }
}
