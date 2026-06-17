package com.mefy.platemate.presentation.features.main.profile.settings.changepassword.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.profile.settings.changepassword.ChangePasswordUiState
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ValidationChecklist(state: ChangePasswordUiState) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .border(dims.stroke.st1, colors.cardBorder, MaterialTheme.shapes.medium)
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s12),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
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
