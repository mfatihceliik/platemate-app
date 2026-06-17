package com.mefy.platemate.presentation.components

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import com.mefy.platemate.presentation.components.model.PMTextStyle

internal fun PMTextStyle.resolve(typography: Typography): TextStyle = when (this) {
    PMTextStyle.Display -> typography.displaySmall
    PMTextStyle.Headline -> typography.headlineSmall
    PMTextStyle.Title -> typography.titleMedium
    PMTextStyle.Body -> typography.bodyLarge
    PMTextStyle.Label -> typography.labelLarge
    PMTextStyle.Caption -> typography.bodySmall
    PMTextStyle.Note -> typography.labelSmall
    PMTextStyle.SectionLabel -> typography.labelMedium
}
