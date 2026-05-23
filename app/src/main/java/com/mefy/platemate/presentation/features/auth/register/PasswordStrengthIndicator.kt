package com.mefy.platemate.presentation.features.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.mefy.platemate.presentation.components.PMTextStyle
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
    val (strengthText, fillColor) = strength.toUiModel()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.s4)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(spacing.s4)
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(radius.r8)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(strength.progress)
                    .height(spacing.s4)
                    .background(
                        color = fillColor,
                        shape = RoundedCornerShape(radius.r8)
                    )
            )
        }

        PMText(
            text = strengthText,
            style = PMTextStyle.Caption,
            color = fillColor,
            modifier = Modifier.padding(horizontal = spacing.s2)
        )
    }
}

@Composable
private fun PasswordStrength.toUiModel(): Pair<String, Color> {
    return when (level) {
        PasswordStrengthLevel.WEAK -> {
            stringResource(R.string.auth_register_password_strength_weak) to MaterialTheme.colorScheme.error
        }

        PasswordStrengthLevel.MEDIUM -> {
            stringResource(R.string.auth_register_password_strength_medium) to MaterialTheme.colorScheme.tertiary
        }

        PasswordStrengthLevel.STRONG -> {
            stringResource(R.string.auth_register_password_strength_strong) to MaterialTheme.colorScheme.primary
        }

        PasswordStrengthLevel.NONE -> {
            "" to MaterialTheme.colorScheme.onSurfaceVariant
        }
    }
}
