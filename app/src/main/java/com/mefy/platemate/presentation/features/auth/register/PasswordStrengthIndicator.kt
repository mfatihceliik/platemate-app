package com.mefy.platemate.presentation.features.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PasswordStrengthIndicator(
    strength: PasswordStrength,
    modifier: Modifier = Modifier
) {
    if (strength.level == PasswordStrengthLevel.NONE) {
        return
    }

    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    val radius = dimensions.radius
    val stroke = dimensions.stroke
    val colors = MaterialTheme.pmColors
    val (strengthText, fillColor) = strength.toUiModel()

    val defaultColor = colors.outlineVariant
    val textColor = colors.onSurfaceVariant
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.s4)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            val totalSegments = 4
            val activeSegments = (strength.progress * totalSegments).toInt().coerceIn(1, 4)
            
            for (i in 0 until totalSegments) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(spacing.s4)
                        .background(
                            color = if (i < activeSegments) fillColor else defaultColor,
                            shape = RoundedCornerShape(radius.r8)
                        )
                )
            }
        }

        PMText(
            text = strengthText,
            style = PMTextStyle.Caption,
            color = textColor,
            modifier = Modifier.padding(horizontal = spacing.s4)
        )
    }
}

@Composable
private fun PasswordStrength.toUiModel(): Pair<String, Color> {
    return when (level) {
        PasswordStrengthLevel.WEAK -> {
            stringResource(R.string.auth_register_password_strength_weak_hint) to MaterialTheme.pmColors.primary
        }

        PasswordStrengthLevel.MEDIUM -> {
            stringResource(R.string.auth_register_password_strength_medium) to MaterialTheme.pmColors.primary
        }

        PasswordStrengthLevel.STRONG -> {
            stringResource(R.string.auth_register_password_strength_strong) to MaterialTheme.pmColors.primary
        }

        PasswordStrengthLevel.NONE -> {
            "" to MaterialTheme.pmColors.onSurfaceVariant
        }
    }
}
