package com.mefy.platemate.presentation.features.main.settings.changepassword.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.settings.changepassword.ChangePasswordUiState
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun ValidationChecklist(state: ChangePasswordUiState) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shapes = PMTheme.shapes
    val stroke = PMTheme.stroke

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, shapes.medium)
            .border(stroke.st1, colors.cardBorder, shapes.medium)
            .padding(horizontal = spacing.s12, vertical = spacing.s12),
        verticalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        ValidationRule(
            text = stringResource(R.string.profile_change_password_rule_length),
            passed = state.hasMinLength
        )
        ValidationRule(
            text = stringResource(R.string.profile_change_password_rule_uppercase),
            passed = state.hasUppercase
        )
        ValidationRule(
            text = stringResource(R.string.profile_change_password_rule_digit),
            passed = state.hasDigit
        )
    }
}
