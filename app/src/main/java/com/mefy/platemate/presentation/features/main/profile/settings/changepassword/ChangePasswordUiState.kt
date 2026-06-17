package com.mefy.platemate.presentation.features.main.profile.settings.changepassword

import androidx.compose.runtime.Immutable

@Immutable
data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isSubmitting: Boolean = false
) {
    val hasMinLength: Boolean get() = newPassword.length >= 8
    val hasUppercase: Boolean get() = newPassword.any { it.isUpperCase() }
    val hasDigit: Boolean get() = newPassword.any { it.isDigit() }
    val passwordsMatch: Boolean get() = newPassword == confirmPassword && confirmPassword.isNotEmpty()

    val isSubmitEnabled: Boolean
        get() = currentPassword.isNotBlank() &&
                hasMinLength && hasUppercase && hasDigit &&
                passwordsMatch && !isSubmitting
}
